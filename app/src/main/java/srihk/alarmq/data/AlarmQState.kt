package srihk.alarmq.data

data class AlarmQState(
    val isActive: Boolean = false,
    val intervalQueueContents: List<Int> = emptyList(),
    val currentInterval: Int? = null,
    val nextAlarmScheduledTime: String? = null
)
