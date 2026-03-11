package srihk.alarmq

import android.content.Context
import android.content.Intent
import android.os.Build

class AndroidRingtoneAlarmRinger(private val context: Context) : AlarmRinger {
    val ringtoneServiceIntent = Intent(context, RingtoneService::class.java)

    override fun start() {
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