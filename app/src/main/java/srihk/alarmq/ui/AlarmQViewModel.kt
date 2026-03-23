package srihk.alarmq.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import srihk.alarmq.domain.AlarmQManager
import srihk.alarmq.app.AlarmQApplication
import srihk.alarmq.data.AlarmQState
import srihk.alarmq.data.AlarmQStateRepository
import srihk.alarmq.data.Interval

class AlarmQViewModel(
    private val alarmQStateRepository: AlarmQStateRepository,
    private val alarmQManager: AlarmQManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val alarmQStateFlow: StateFlow<AlarmQState> = alarmQStateRepository.alarmQFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AlarmQState()
        )

    val intervalListStateFlow = alarmQStateRepository.intervalListFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteInterval(interval: Interval) {
        viewModelScope.launch {
            alarmQStateRepository.deleteInterval(interval)
        }
    }

    fun addInterval(intervalDuration: Int) {
        viewModelScope.launch {
            alarmQStateRepository.addInterval(intervalDuration)
        }
    }

    fun editInterval(interval: Interval) {
        viewModelScope.launch {
            alarmQStateRepository.editInterval(interval)
        }
    }

    fun startAlarmQ() {
        viewModelScope.launch {
            alarmQManager.start()
        }
    }

    fun stopAlarmQ() {
        viewModelScope.launch {
            alarmQManager.stop()
        }
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
                    alarmQManager = application.alarmQManager,
                    savedStateHandle
                ) as T
            }
        }
    }
}