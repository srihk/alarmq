package srihk.alarmq.data

import kotlinx.coroutines.flow.StateFlow

interface AlarmQDataSource {
    val stateFlow: StateFlow<AlarmQState>
    fun getAlarmQState(): AlarmQState
    fun saveAlarmQState(state: AlarmQState)
}