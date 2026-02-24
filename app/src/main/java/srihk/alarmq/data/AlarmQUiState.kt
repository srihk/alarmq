package srihk.alarmq.data

data class AlarmQUiState(
    val isActive: Boolean = false,
    val intervalQueueContents: List<Int> = emptyList(),
    val currentInterval: Int? = null,
    val nextAlarmScheduledTime: String? = null
)
