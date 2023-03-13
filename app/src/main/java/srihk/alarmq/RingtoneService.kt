package srihk.alarmq

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import srihk.alarmq.Constants.NOTIFICATION_ID

class RingtoneService : Service() {
    private var alarmRingtone: Ringtone? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        alarmRingtone = RingtoneManager.getRingtone(
            this,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        )

        if (alarmRingtone == null) {
            Toast.makeText(
                this,
                "Please set a default Alarm Ringtone.",
                Toast.LENGTH_LONG
            ).show()
            // TODO: Facilitate the user to set default alarm.
        }

        val notificationManager: NotificationManager = this.getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Alarm Ringing!", Toast.LENGTH_SHORT).show()
        alarmRingtone?.play()
        startForeground(NOTIFICATION_ID, buildNotification(this))
        return START_STICKY
    }

    override fun onDestroy() {
        alarmRingtone?.stop()
        Toast.makeText(this, "Alarm Stopped", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private fun buildNotification(context: Context): Notification {
        val snoozeIntent = Intent(context, AlarmQ::class.java).putExtra(
            Constants.ACTION,
            Constants.SNOOZE
        )
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = Intent(context, AlarmQ::class.java).putExtra(
            Constants.ACTION,
            Constants.STOP
        )
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_UPDATE_CURRENT
        )

        val openIntent = Intent(context, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val openPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Alarm ringing!")
            .setContentIntent(openPendingIntent)
            .addAction(R.drawable.ic_launcher_background, Constants.SNOOZE, snoozePendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, Constants.STOP, stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)

        return builder.build()
    }
}