package srihk.alarmq

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class AndroidAlarmScheduler(private val context: Context) : AlarmScheduler {

    var messageDisplayer: MessageDisplayer = (context.applicationContext as AlarmQApplication).messageDisplayer
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleAlarm(time: Long): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                messageDisplayer.showLong("Please give AlarmQ the permission 'Allow setting alarms and remainders'.")
                return false
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

        return true
    }

    override fun clearScheduledAlarms() {
        val intent = Intent(context, AlarmQ::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}