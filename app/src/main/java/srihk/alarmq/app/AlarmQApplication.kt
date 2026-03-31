package srihk.alarmq.app

import android.app.Application
import androidx.room.Room
import srihk.alarmq.domain.AlarmQManager
import srihk.alarmq.alarm.AlarmRinger
import srihk.alarmq.alarm.AlarmScheduler
import srihk.alarmq.data.AlarmQDao
import srihk.alarmq.data.AlarmQDatabase
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

    val database by lazy {
        Room.databaseBuilder<AlarmQDatabase>(
                this,
                "alarmq_db"
            ).fallbackToDestructiveMigration(false).build()
    }

    val alarmQDao: AlarmQDao by lazy {
        database.alarmQDao()
    }

    override fun onCreate() {
        super.onCreate()
        messageDisplayer = ToastMessageDisplayer(this)
        val localAlarmQDataSource = LocalAlarmQDataSource(alarmQDao)
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