package srihk.alarmq.domain

import srihk.alarmq.data.AlarmQStateRepository

class AlarmQEditor(
    private val alarmQStateRepository: AlarmQStateRepository
) {
    private val stateFlow = alarmQStateRepository.stateFlow

    fun deleteIntervalAtIndex(id: Int) {
        if (stateFlow.value.isActive) return
        alarmQStateRepository.deleteIntervalAtIndex(id)
    }

    fun addInterval(intervalDuration: Int) {
        if (stateFlow.value.isActive) return
        alarmQStateRepository.addInterval(intervalDuration)
    }

    fun editInterval(id: Int, newIntervalDuration: Int) {
        if (stateFlow.value.isActive) return
        alarmQStateRepository.editInterval(id, newIntervalDuration)
    }
}