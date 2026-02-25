package srihk.alarmq.data

import android.content.Context
import srihk.alarmq.Preferences

class LocalAlarmQDataSource(private val context: Context) : AlarmQDataSource {
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
}