package srihk.alarmq.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow

class AlarmQStateRepository(
    private val alarmQDataSource: AlarmQDataSource
) {
    val alarmQFlow: Flow<AlarmQState> = alarmQDataSource.alarmQFlow
    val intervalListFlow = alarmQDataSource.intervalListFlow

    val settingsFlow = alarmQDataSource.settingsFlow

    suspend fun getCurrentAlarmQState(): AlarmQState {
        return alarmQDataSource.getCurrentAlarmQState()
    }

    suspend fun getCurrentIntervalListState(): List<Interval> {
        return alarmQDataSource.getCurrentIntervalListState()
    }

    suspend fun deleteInterval(interval: Interval) {
        alarmQDataSource.deleteInterval(interval)
    }

    suspend fun addInterval(interval: Interval) {
        alarmQDataSource.insertInterval(interval)
    }

    suspend fun editInterval(interval: Interval) {
        alarmQDataSource.updateInterval(interval)
    }

    suspend fun setActiveInterval(currentInterval: Int, nextAlarmScheduledTime: Long) {
        alarmQDataSource.saveAlarmQState(
            AlarmQState(true, currentInterval, nextAlarmScheduledTime)
        )
    }

    suspend fun resetState() {
        alarmQDataSource.saveAlarmQState(
            AlarmQState(
                isActive = false,
                currentInterval = null,
                nextAlarmTime = null
            )
        )
    }

    suspend fun setDefaultRingtone(uri: Uri?) {
        alarmQDataSource.setDefaultRingtone(uri)
    }

    suspend fun getDefaultRingtone(): Uri? {
        return alarmQDataSource.getDefaultRingtone()
    }
}