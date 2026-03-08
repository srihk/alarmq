package srihk.alarmq

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.StateFlow
import srihk.alarmq.data.AlarmQState
import srihk.alarmq.data.AlarmQStateRepository

class AlarmQViewModel(
    private val alarmQStateRepository: AlarmQStateRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiState: StateFlow<AlarmQState> = alarmQStateRepository.stateFlow

    fun saveState(alarmQState: AlarmQState) {
        alarmQStateRepository.saveState(alarmQState)
    }

    fun deleteIntervalAtIndex(id: Int) {
        val newState = uiState.value.copy(
            isActive = uiState.value.isActive,
            intervalQueueContents = uiState.value.intervalQueueContents.filterIndexed { index, _ -> index != id },
            currentInterval = uiState.value.currentInterval,
            nextAlarmScheduledTime = uiState.value.nextAlarmScheduledTime
        )

        alarmQStateRepository.saveState(newState)
    }

    fun addInterval(intervalDuration: Int) {
        val newState = uiState.value.copy(
            isActive = uiState.value.isActive,
            intervalQueueContents = uiState.value.intervalQueueContents.plus(intervalDuration),
            currentInterval = uiState.value.currentInterval,
            nextAlarmScheduledTime = uiState.value.nextAlarmScheduledTime
        )

        alarmQStateRepository.saveState(newState)
    }

    fun editInterval(id: Int, newIntervalDuration: Int) {
        val newState = uiState.value.copy(
            isActive = uiState.value.isActive,
            intervalQueueContents = uiState.value.intervalQueueContents.mapIndexed { index, value ->
                if (index == id) {
                    newIntervalDuration
                } else {
                    value
                }
            },
            currentInterval = uiState.value.currentInterval,
            nextAlarmScheduledTime = uiState.value.nextAlarmScheduledTime
        )

        alarmQStateRepository.saveState(newState)
    }

    companion object : ViewModelProvider.Factory {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return AlarmQViewModel(
                    alarmQStateRepository = (application as AlarmQApplication).alarmQStateRepository,
                    savedStateHandle
                ) as T
            }
        }
    }
}
