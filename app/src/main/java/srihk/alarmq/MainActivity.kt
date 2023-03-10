package srihk.alarmq

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import srihk.alarmq.Preferences.PREFERENCES_NAME
import srihk.alarmq.Preferences.getIsRunning
import srihk.alarmq.Preferences.getList
import srihk.alarmq.Preferences.getState
import srihk.alarmq.Preferences.setIsRunning
import srihk.alarmq.Preferences.setList
import srihk.alarmq.Preferences.setState
import srihk.alarmq.ui.AlarmQComposable
import srihk.alarmq.ui.SnoozeItem
import srihk.alarmq.ui.theme.AlarmQTheme

class MainActivity : ComponentActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val snoozeList = mutableStateListOf<String>()
    private val state = mutableStateOf(0)
    private val isRunning = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        snoozeList.addAll(getList(this))
        val alarmQ = AlarmQ()
        getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(this)
        state.value = getState(this)
        isRunning.value = getIsRunning(this)
        setContent {
            AlarmQTheme {
                AlarmQComposable(
                    modifier = Modifier.fillMaxSize(),
                    snoozeList,
                    isRunning = isRunning.value,
                    state = state.value,
                    updatePrefs = {
                        setList(this, snoozeList)
                    },
                    onStart = {
                        if (snoozeList.size == 0) {
                            Toast.makeText(this,
                                "Add at least one snooze item in the queue.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else {
                            if (isRunning.value) { /* Stop */
                                alarmQ.removeAlarm(this)
                            } else { /* Start */
                                alarmQ.setAlarm(
                                    this,
                                    snoozeList[0].toInt()
                                )
                            }
                            isRunning.value = !isRunning.value
                            setIsRunning(this, isRunning = isRunning.value)
                            setState(this, 0)
                        }
                    }
                )
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        state.value = getState(this)
        isRunning.value = getIsRunning(this)
    }

    override fun onDestroy() {
        getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}

@Preview(showBackground = true, widthDp = 540)
@Composable
fun DefaultPreview() {
    AlarmQTheme {
        SnoozeItem(
            name = "hi",
            item = "23",
            false,
            {},
            {},
            MaterialTheme.colorScheme.primary
        )
//        InputDialog(true, {})
//        AlarmQComposable()
    }
}