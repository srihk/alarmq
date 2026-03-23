package srihk.alarmq.data.mapper

import srihk.alarmq.data.AlarmQState
import srihk.alarmq.data.AlarmQStateEntity

fun AlarmQStateEntity.toDomain(): AlarmQState {
    return AlarmQState(
        isActive = isActive,
        currentInterval = currentIntervalId,
        nextAlarmTime = nextAlarmTime
    )
}