package srihk.alarmq.ui

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import srihk.alarmq.app.Constants
import srihk.alarmq.receivers.NotificationActionReceiver
import srihk.alarmq.ui.theme.AlarmQTheme
import kotlin.jvm.java

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nextIntent = Intent(this, NotificationActionReceiver::class.java).putExtra(
            Constants.ACTION,
            Constants.NEXT
        )
        val nextPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            nextIntent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = Intent(this, NotificationActionReceiver::class.java).putExtra(
            Constants.ACTION,
            Constants.STOP
        )
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE + PendingIntent.FLAG_UPDATE_CURRENT
        )

        setContent {
            AlarmQTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                        Text(
                            "Alarm Ringing!",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
                        )
                        Button (
                            modifier = Modifier.weight(1f).fillMaxSize().padding(20.dp),
                            onClick = {
                                nextPendingIntent.send()
                                finish()
                            }
                        ) {
                            Text(Constants.NEXT)
                        }

                        Button(
                            modifier = Modifier.weight(1f).fillMaxSize().padding(20.dp),
                            onClick = {
                                stopPendingIntent.send()
                                finish()
                            }
                        ) {
                            Text(Constants.STOP)
                        }
                    }
                }
            }
        }
    }
}