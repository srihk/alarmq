package srihk.alarmq.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import srihk.alarmq.app.AlarmQApplication
import srihk.alarmq.app.Constants

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val action = intent.getStringExtra(Constants.ACTION)
            val alarmQManager = (context.applicationContext as AlarmQApplication)
                .alarmQManager

            val pendingResult = goAsync()

            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            scope.launch {
                try {
                    if (action.equals(Constants.NEXT)) {
                        alarmQManager.scheduleNextAlarm()
                    } else if (action.equals(Constants.STOP)) {
                        alarmQManager.stop()
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}