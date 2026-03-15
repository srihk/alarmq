package srihk.alarmq.domain

import android.icu.text.SimpleDateFormat
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
    private val alarmQStateFlow = alarmQStateRepository.stateFlow

    private fun scheduleAlarmForInterval(currentInterval: Int = 0) {
        alarmRinger.stop()
        val alarmQState = alarmQStateFlow.value
        if (alarmQState.intervalQueueContents.isEmpty()) {
            messageDisplayer.showLong("Add at least one snooze item in the queue.")
            return
        }

        val time: Long = System.currentTimeMillis() + alarmQState.intervalQueueContents[currentInterval] * 60000
        if (!alarmScheduler.scheduleAlarm(time)) {
            messageDisplayer.showLong("Couldn't schedule alarm.")
            return
        }

        alarmQStateRepository.setActiveInterval(
            currentInterval,
            SimpleDateFormat.getDateTimeInstance().format(time)
        )
    }

    fun start() {
        scheduleAlarmForInterval(0)
    }

    fun stop() {
        alarmScheduler.clearScheduledAlarms()
        alarmRinger.stop()
        alarmQStateRepository.resetState()
    }

    fun scheduleNextAlarm() {
        val alarmQState = alarmQStateFlow.value
        if (alarmQState.currentInterval == null) {
            start()
            return
        }

        scheduleAlarmForInterval(
            (alarmQState.currentInterval + 1) % alarmQState.intervalQueueContents.size
        )
    }
}