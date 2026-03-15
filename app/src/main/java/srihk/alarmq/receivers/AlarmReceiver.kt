package srihk.alarmq.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import srihk.alarmq.infrastructure.AndroidRingtoneAlarmRinger
import srihk.alarmq.alarm.AlarmRinger

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
         if (context != null) {
             val alarmRinger: AlarmRinger = AndroidRingtoneAlarmRinger(context)
             alarmRinger.start()
         }
    }
}
