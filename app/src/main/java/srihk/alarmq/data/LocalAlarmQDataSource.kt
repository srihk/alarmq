package srihk.alarmq.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import srihk.alarmq.infrastructure.Preferences
import srihk.alarmq.infrastructure.Preferences.PREFERENCES_NAME

class LocalAlarmQDataSource(private val context: Context) : AlarmQDataSource, SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(this)
    }

    private val _stateFlow = MutableStateFlow(getAlarmQState())
    override val stateFlow : StateFlow<AlarmQState> = _stateFlow.asStateFlow()

    override fun getAlarmQState(): AlarmQState {
        val state = AlarmQState(
            Preferences.getIsRunning(context),
            Preferences.getList(context),
            Preferences.getState(context),
            Preferences.getNextAlarm(context)
        )

        return state;
    }

    override fun saveAlarmQState(state: AlarmQState) {
        Preferences.setIsRunning(context, state.isActive)
        Preferences.setList(context, state.intervalQueueContents)
        Preferences.setState(context, state.currentInterval ?: 0)
        Preferences.setNextAlarm(context, state.nextAlarmScheduledTime ?: "")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        _stateFlow.update { getAlarmQState() }
    }
}