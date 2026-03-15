package srihk.alarmq.data

import kotlinx.coroutines.flow.StateFlow

class AlarmQStateRepository(
    private val alarmQDataSource: AlarmQDataSource
) {
    val stateFlow: StateFlow<AlarmQState> = alarmQDataSource.stateFlow

    fun deleteIntervalAtIndex(id: Int) {
        val state = stateFlow.value
        alarmQDataSource.saveAlarmQState(
            state.copy(
                intervalQueueContents = state.intervalQueueContents.filterIndexed { index, _ -> index != id }
            )
        )
    }

    fun addInterval(intervalDuration: Int) {
        val state = stateFlow.value
        alarmQDataSource.saveAlarmQState(
            state.copy(
                intervalQueueContents = state.intervalQueueContents.plus(intervalDuration)
            )
        )
    }

    fun editInterval(id: Int, newIntervalDuration: Int) {
        val state = stateFlow.value
        alarmQDataSource.saveAlarmQState(
            state.copy(
                intervalQueueContents = state.intervalQueueContents.mapIndexed { index, value ->
                    if (index == id) {
                        newIntervalDuration
                    } else {
                        value
                    }
                }
            )
        )
    }

    fun setActiveInterval(currentInterval: Int, nextAlarmScheduledTime: String) {
        val state = stateFlow.value
        alarmQDataSource.saveAlarmQState(
            state.copy(
                isActive = true,
                currentInterval = currentInterval,
                nextAlarmScheduledTime = nextAlarmScheduledTime
            )
        )
    }

    fun resetState() {
        val state = stateFlow.value
        alarmQDataSource.saveAlarmQState(
            state.copy(
                isActive = false,
                currentInterval = null,
                nextAlarmScheduledTime = null
            )
        )
    }
}