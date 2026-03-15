package srihk.alarmq.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.StateFlow
import srihk.alarmq.domain.AlarmQEditor
import srihk.alarmq.domain.AlarmQManager
import srihk.alarmq.app.AlarmQApplication
import srihk.alarmq.data.AlarmQState
import srihk.alarmq.data.AlarmQStateRepository

class AlarmQViewModel(
    private val alarmQStateRepository: AlarmQStateRepository,
    private val alarmQEditor: AlarmQEditor,
    private val alarmQManager: AlarmQManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiState: StateFlow<AlarmQState> = alarmQStateRepository.stateFlow

    fun deleteIntervalAtIndex(id: Int) {
        alarmQEditor.deleteIntervalAtIndex(id)
    }

    fun addInterval(intervalDuration: Int) {
        alarmQEditor.addInterval(intervalDuration)
    }

    fun editInterval(id: Int, newIntervalDuration: Int) {
        alarmQEditor.editInterval(id, newIntervalDuration)
    }

    fun startAlarmQ() {
        alarmQManager.start()
    }

    fun stopAlarmQ() {
        alarmQManager.stop()
    }

    companion object : ViewModelProvider.Factory {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return AlarmQViewModel(
                    alarmQStateRepository = (application as AlarmQApplication).alarmQStateRepository,
                    alarmQEditor = (application as AlarmQApplication).alarmQEditor,
                    alarmQManager = (application as AlarmQApplication).alarmQManager,
                    savedStateHandle
                ) as T
            }
        }
    }
}