package srihk.alarmq.domain

import srihk.alarmq.alarm.AlarmRinger
import srihk.alarmq.alarm.AlarmScheduler
import srihk.alarmq.feedback.MessageDisplayer
import srihk.alarmq.data.AlarmQStateRepository

class AlarmQManager(
    private val alarmScheduler : AlarmScheduler,
    private val alarmQStateRepository: AlarmQStateRepository,
    private val messageDisplayer: MessageDisplayer,
    private val alarmRinger: AlarmRinger
) {
    private suspend fun scheduleAlarmForInterval(currentInterval: Int = 0) {
        alarmRinger.stop()
        val intervalListState = alarmQStateRepository.getCurrentIntervalListState()
        if (intervalListState.isEmpty()) {
            messageDisplayer.showLong("Add at least one snooze item in the queue.")
            return
        }

        val time: Long = System.currentTimeMillis() + intervalListState[currentInterval].duration * 60000
        if (!alarmScheduler.scheduleAlarm(time, intervalListState[currentInterval].ringtoneUri)) {
            messageDisplayer.showLong("Couldn't schedule alarm.")
            return
        }

        alarmQStateRepository.setActiveInterval(
            currentInterval,
            time
        )
    }

    suspend fun start() {
        scheduleAlarmForInterval(0)
    }

    suspend fun stop() {
        alarmScheduler.clearScheduledAlarms()
        alarmRinger.stop()
        alarmQStateRepository.resetState()
    }

    suspend fun scheduleNextAlarm() {
        val alarmQState = alarmQStateRepository.getCurrentAlarmQState()
        val intervalListState = alarmQStateRepository.getCurrentIntervalListState()
        if (alarmQState.currentInterval == null) {
            start()
            return
        }

        scheduleAlarmForInterval(
            (alarmQState.currentInterval + 1) % intervalListState.size
        )
    }
}