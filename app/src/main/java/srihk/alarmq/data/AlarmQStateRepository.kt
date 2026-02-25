package srihk.alarmq.data

class AlarmQStateRepository(
    private val alarmQDataSource: AlarmQDataSource
) {
    fun getState(): AlarmQState {
        return alarmQDataSource.getAlarmQState()
    }

    fun saveState(state: AlarmQState) {
        alarmQDataSource.saveAlarmQState(state)
    }
}