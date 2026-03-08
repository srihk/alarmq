package srihk.alarmq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import srihk.alarmq.ui.AlarmQComposable
import srihk.alarmq.ui.SnoozeItem
import srihk.alarmq.ui.theme.AlarmQTheme

class MainActivity : ComponentActivity() {
    private val messageDisplayer by lazy {
        (application as AlarmQApplication).messageDisplayer
    }

    private val viewModel: AlarmQViewModel by viewModels { AlarmQViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmQ = AlarmQ()
        val alarmQState = viewModel.uiState
        setContent {
            AlarmQTheme {
                AlarmQComposable(
                    modifier = Modifier.fillMaxSize(),
                    onStart = {
                        if (alarmQState.value.intervalQueueContents.isEmpty()) {
                            messageDisplayer.showLong("Add at least one snooze item in the queue.")
                        }
                        else {
                            if (
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                                PackageManager.PERMISSION_DENIED
                            ) {
                                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),0)
                            } else {
                                if (alarmQState.value.isActive) { /* Stop */
                                    alarmQ.removeAlarm(this, messageDisplayer)
                                } else { /* Start */
                                    alarmQ.setAlarm(
                                        this,
                                        alarmQState.value.intervalQueueContents[0],
                                        messageDisplayer
                                    )
                                }

                                viewModel.saveState(
                                    alarmQState.value.copy(
                                        isActive = !alarmQState.value.isActive,
                                        intervalQueueContents = alarmQState.value.intervalQueueContents,
                                        currentInterval = 0,
                                        nextAlarmScheduledTime = alarmQState.value.nextAlarmScheduledTime
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 540)
@Composable
fun DefaultPreview() {
    AlarmQTheme {
        SnoozeItem(
            name = "hi",
            item = 23,
            false,
            {},
            {},
            MaterialTheme.colorScheme.primary
        )
//        InputDialog(true, {})
//        AlarmQComposable()
    }
}