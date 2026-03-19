package srihk.alarmq.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import srihk.alarmq.data.mapper.toDomain
import srihk.alarmq.data.mapper.toEntity

class LocalAlarmQDataSource(private val alarmQDao: AlarmQDao) : AlarmQDataSource {
    override val alarmQFlow : Flow<AlarmQState> = alarmQDao.getAlarmQState().map { stateEntity ->
        AlarmQState(
            stateEntity?.isActive ?: false,
            stateEntity?.currentIntervalId,
            stateEntity?.nextAlarmTime
        )
    }

    override val intervalListFlow : Flow<List<Interval>> = alarmQDao.getIntervals()
        .map { intervalEntities ->
            intervalEntities.map { intervalEntity -> intervalEntity.toDomain() }
        }

    override suspend fun saveAlarmQState(state: AlarmQState) {
        alarmQDao.setAlarmQState(
            AlarmQStateEntity(
                isActive = state.isActive,
                currentIntervalId = state.currentInterval,
                nextAlarmTime = state.nextAlarmTime
            )
        )
    }

    override suspend fun insertInterval(interval: Interval) {
        alarmQDao.insertInterval(interval.toEntity())
    }

    override suspend fun deleteInterval(interval: Interval) {
        alarmQDao.deleteInterval(interval.toEntity())
    }

    override suspend fun updateInterval(interval: Interval) {
        alarmQDao.updateInterval(interval.toEntity())
    }
}