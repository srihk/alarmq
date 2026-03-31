package srihk.alarmq.infrastructure

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import srihk.alarmq.alarm.AlarmRinger

class AndroidRingtoneAlarmRinger(private val context: Context) : AlarmRinger {
    private val ringtoneServiceIntent = Intent(context, RingtoneService::class.java)
    override fun start(uri: Uri?) {
        if (uri != null)
            ringtoneServiceIntent.putExtra("uri", uri.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(ringtoneServiceIntent)
        } else {
            context.startService(ringtoneServiceIntent)
        }
    }

    override fun stop() {
        context.stopService(ringtoneServiceIntent)
    }
}