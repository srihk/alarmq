package srihk.alarmq

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.getStringExtra(Constants.ACTION)
            val alarmQManager = (context.applicationContext as AlarmQApplication)
                .alarmQManager

            if (action.equals(Constants.SNOOZE)) {
                alarmQManager.scheduleNextAlarm()
            } else if (action.equals(Constants.STOP)) {
                alarmQManager.stop()
            }
        }
    }
}