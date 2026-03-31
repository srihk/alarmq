package srihk.alarmq.alarm

import android.net.Uri

interface AlarmRinger {
    fun start(uri: Uri?)
    fun stop()
}