package srihk.alarmq

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlinx.coroutines.flow.StateFlow
import srihk.alarmq.data.AlarmQState
import java.text.SimpleDateFormat

class AlarmQ : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val ringtoneServiceIntent = Intent(context, RingtoneService::class.java)

        if (intent != null && context != null) {
            val messageDisplayer = (context.applicationContext as AlarmQApplication).messageDisplayer
            val alarmQStateRepository = (context.applicationContext as AlarmQApplication).alarmQStateRepository
            val action = intent.getStringExtra(Constants.ACTION)
            val alarmQState: StateFlow<AlarmQState> = alarmQStateRepository.stateFlow

            if (action.equals(Constants.SNOOZE)) {
                context.stopService(ringtoneServiceIntent) /* Stop Alarm */
                val currentInterval = alarmQState.value.currentInterval
                val nextInterval = if (currentInterval == null) 0 else ((currentInterval + 1) % alarmQState.value.intervalQueueContents.size)
                setAlarm(
                    context,
                    alarmQState.value.intervalQueueContents[nextInterval],
                    messageDisplayer
                )
                alarmQStateRepository.saveState(
                    alarmQState.value.copy(
                        currentInterval = nextInterval
                    )
                )
                return
            } else if (action.equals(Constants.STOP)) {
                alarmQStateRepository.saveState(
                    alarmQState.value.copy(
                        isActive = false
                    )
                )
                context.stopService(ringtoneServiceIntent) /* Stop Alarm */
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(ringtoneServiceIntent)
        } else {
            context?.startService(ringtoneServiceIntent)
        } /* Start Alarm */
    }

    fun setAlarm(context: Context, minutes: Int, messageDisplayer: MessageDisplayer) {
        val time: Long = System.currentTimeMillis() + minutes * 60000
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                messageDisplayer.showLong("Please give AlarmQ the permission 'Allow setting alarms and remainders'.")
                return
            }
        }

        val intent = Intent(context, AlarmQ::class.java)
            .putExtra("time", time)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_UPDATE_CURRENT
        )
        val info: AlarmManager.AlarmClockInfo = AlarmManager.AlarmClockInfo(time, pendingIntent)
        alarmManager.setAlarmClock(info, pendingIntent)
        val nextAlarm = SimpleDateFormat.getDateTimeInstance().format(time)
        Preferences.setNextAlarm(context, nextAlarm)
        messageDisplayer.showLong("Alarm set for $minutes minutes from now: ${nextAlarm}.")
    }

    fun removeAlarm(context: Context, messageDisplayer: MessageDisplayer) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmQ::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)

        val ringtoneServiceIntent = Intent(context, RingtoneService::class.java)
        context.stopService(ringtoneServiceIntent)

        messageDisplayer.showLong("Cleared alarms.")
    }
}