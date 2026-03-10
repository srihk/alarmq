package srihk.alarmq

interface AlarmScheduler {
    fun scheduleAlarm(time: Long): Boolean
    fun clearScheduledAlarms()
}