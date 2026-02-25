package srihk.alarmq

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import srihk.alarmq.data.AlarmQState

class AlarmQViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AlarmQState())
    val uiState: StateFlow<AlarmQState> = _uiState.asStateFlow()
}
