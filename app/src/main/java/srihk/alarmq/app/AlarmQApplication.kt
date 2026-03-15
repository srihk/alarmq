package srihk.alarmq.app

import android.app.Application
import srihk.alarmq.domain.AlarmQEditor
import srihk.alarmq.domain.AlarmQManager
import srihk.alarmq.alarm.AlarmRinger
import srihk.alarmq.alarm.AlarmScheduler
import srihk.alarmq.infrastructure.AndroidAlarmScheduler
import srihk.alarmq.infrastructure.AndroidRingtoneAlarmRinger
import srihk.alarmq.feedback.MessageDisplayer
import srihk.alarmq.feedback.ToastMessageDisplayer
import srihk.alarmq.data.AlarmQStateRepository
import srihk.alarmq.data.LocalAlarmQDataSource

class AlarmQApplication : Application() {
    lateinit var alarmQStateRepository: AlarmQStateRepository
        private set
    lateinit var messageDisplayer: MessageDisplayer
        private set

    lateinit var alarmRinger: AlarmRinger
        private set

    lateinit var alarmScheduler: AlarmScheduler
        private set

    lateinit var alarmQManager: AlarmQManager
        private set

    lateinit var alarmQEditor: AlarmQEditor
        private set

    override fun onCreate() {
        super.onCreate()

        messageDisplayer = ToastMessageDisplayer(applicationContext)
        val localAlarmQDataSource = LocalAlarmQDataSource(this)
        alarmQStateRepository = AlarmQStateRepository(localAlarmQDataSource)
        alarmRinger = AndroidRingtoneAlarmRinger(this)
        alarmScheduler = AndroidAlarmScheduler(this)
        alarmQManager = AlarmQManager(
            alarmScheduler,
            alarmQStateRepository,
            messageDisplayer,
            alarmRinger
        )
        alarmQEditor = AlarmQEditor(
            alarmQStateRepository
        )
    }
}