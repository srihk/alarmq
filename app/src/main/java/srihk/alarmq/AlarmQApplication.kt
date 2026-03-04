package srihk.alarmq

import android.app.Application
import srihk.alarmq.Preferences.PREFERENCES_NAME
import srihk.alarmq.data.AlarmQStateRepository
import srihk.alarmq.data.LocalAlarmQDataSource

class AlarmQApplication : Application() {
    lateinit var alarmQStateRepository: AlarmQStateRepository
        private set
    lateinit var messageDisplayer: MessageDisplayer
        private set

    override fun onCreate() {
        super.onCreate()

        messageDisplayer = ToastMessageDisplayer(applicationContext)
        val localAlarmQDataSource = LocalAlarmQDataSource(this)
        alarmQStateRepository = AlarmQStateRepository(localAlarmQDataSource)
    }
}