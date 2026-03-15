package srihk.alarmq.alarm

interface AlarmScheduler {
    fun scheduleAlarm(time: Long): Boolean
    fun clearScheduledAlarms()
}