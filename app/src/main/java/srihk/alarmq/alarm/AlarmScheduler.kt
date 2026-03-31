package srihk.alarmq.alarm

import android.net.Uri

interface AlarmScheduler {
    fun scheduleAlarm(time: Long, uri: Uri?): Boolean
    fun clearScheduledAlarms()
}