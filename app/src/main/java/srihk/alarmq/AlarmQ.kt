package srihk.alarmq

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.text.SimpleDateFormat

class AlarmQ : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        val ringtoneServiceIntent = Intent(context, RingtoneService::class.java)

        if (intent != null && context != null) {
            val action = intent.getStringExtra(Constants.ACTION)

            if (action.equals(Constants.SNOOZE)) {
                context.stopService(ringtoneServiceIntent) /* Stop Alarm */
                val snoozeList = Preferences.getList(context)
                var state = Preferences.getState(context)
                state = (state + 1) % snoozeList.size
                setAlarm(
                    context,
                    snoozeList[state])
                Preferences.setState(context, state)
                return
            } else if (action.equals(Constants.STOP)) {
                Preferences.setIsRunning(context, false)
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

    fun setAlarm(context: Context, minutes: Int) {
        val time: Long = System.currentTimeMillis() + minutes * 60000
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(
                    context,
                    "Please give AlarmQ the permission 'Allow setting alarms and remainders'.",
                    Toast.LENGTH_LONG
                ).show()
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
        Toast.makeText(
            context,
            "Alarm set for $minutes minutes from now: ${SimpleDateFormat.getDateTimeInstance().format(time)}.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun removeAlarm(context: Context) {
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

        Toast.makeText(
            context,
            "Cleared alarms.",
            Toast.LENGTH_LONG
        ).show()
    }
}