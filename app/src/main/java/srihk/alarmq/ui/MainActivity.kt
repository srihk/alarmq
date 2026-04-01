package srihk.alarmq.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import srihk.alarmq.app.AlarmQApplication
import srihk.alarmq.data.Interval
import srihk.alarmq.ui.theme.AlarmQTheme

class MainActivity : ComponentActivity() {
    private val messageDisplayer by lazy {
        (application as AlarmQApplication).messageDisplayer
    }

    private val ringtonePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        resultHandler(result)
    }

    private var pendingInterval: Interval? = null

    fun resultHandler(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val uri: Uri? = result.data
                ?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            val interval = pendingInterval
            if (interval != null && uri != null) {
                viewModel.editInterval(interval.copy(
                    ringtoneUri = uri
                ))
            }
        }
    }

    fun openRingtonePicker(interval: Interval) {
        val defaultUri = RingtoneManager.getActualDefaultRingtoneUri(
            this,
            RingtoneManager.TYPE_ALARM
        )

        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone")
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, interval.ringtoneUri?:defaultUri)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        }

        pendingInterval = interval
        ringtonePickerLauncher.launch(intent)
    }

    private val viewModel: AlarmQViewModel by viewModels { AlarmQViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmQStateFlow = viewModel.alarmQStateFlow
        val intervalListStateFlow = viewModel.intervalListStateFlow

        setContent {
            AlarmQTheme {
                AlarmQComposable(
                    modifier = Modifier.fillMaxSize(),
                    onStart = {
                        if (intervalListStateFlow.value.isEmpty()) {
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
                                if (alarmQStateFlow.value.isActive) { /* Stop */
                                    viewModel.stopAlarmQ()
                                } else { /* Start */
                                    viewModel.startAlarmQ()
                                }
                            }
                        }
                    },
                    openRingtonePicker = { interval ->
                        openRingtonePicker(interval)
                    }
                )
            }
        }
    }
}
