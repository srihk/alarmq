package srihk.alarmq.data

interface AlarmQDataSource {
    fun getAlarmQState(): AlarmQState
    fun saveAlarmQState(state: AlarmQState)
}