package srihk.alarmq.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.net.toUri
import srihk.alarmq.infrastructure.AndroidRingtoneAlarmRinger
import srihk.alarmq.alarm.AlarmRinger

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
         if (context != null && intent != null) {
             val uri: Uri = intent.getStringExtra("uri")?.toUri()
                 ?: RingtoneManager.getActualDefaultRingtoneUri(
                 context,
                 RingtoneManager.TYPE_ALARM
             )
             val alarmRinger: AlarmRinger = AndroidRingtoneAlarmRinger(context)
             alarmRinger.start(uri)
         }
    }
}
