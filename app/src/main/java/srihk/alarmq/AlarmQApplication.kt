package srihk.alarmq

import android.app.Application
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
    }
}