package srihk.alarmq.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AlarmQDataSource {
    val alarmQFlow: Flow<AlarmQState>

    val intervalListFlow: Flow<List<Interval>>

    val settingsFlow: Flow<Settings?>
    suspend fun saveAlarmQState(state: AlarmQState)

    suspend fun insertInterval(interval: Interval)

    suspend fun updateInterval(interval: Interval)

    suspend fun deleteInterval(interval: Interval)

    suspend fun getCurrentAlarmQState(): AlarmQState

    suspend fun getCurrentIntervalListState(): List<Interval>

    suspend fun setDefaultRingtone(uri: Uri?)

    suspend fun getDefaultRingtone(): Uri?
}