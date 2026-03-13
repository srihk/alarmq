package srihk.alarmq

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
         if (context != null) {
             val alarmRinger: AlarmRinger = AndroidRingtoneAlarmRinger(context)
             alarmRinger.start()
         }
    }
}
