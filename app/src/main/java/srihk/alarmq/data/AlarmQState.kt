package srihk.alarmq.data

data class AlarmQState(
    val isActive: Boolean = false,
    val currentInterval: Int? = null,
    val nextAlarmTime: Long? = null
)
