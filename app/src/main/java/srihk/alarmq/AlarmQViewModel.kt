package srihk.alarmq

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import srihk.alarmq.data.AlarmQUiState

class AlarmQViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AlarmQUiState())
    val uiState: StateFlow<AlarmQUiState> = _uiState.asStateFlow()
}
