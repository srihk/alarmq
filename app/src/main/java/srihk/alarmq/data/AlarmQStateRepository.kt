package srihk.alarmq.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlarmQStateRepository(
    private val alarmQDataSource: AlarmQDataSource
) {
    val stateFlow : StateFlow<AlarmQState> = alarmQDataSource.stateFlow

    fun saveState(state: AlarmQState) {
        alarmQDataSource.saveAlarmQState(state)
    }
}