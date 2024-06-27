package srihk.alarmq

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import srihk.alarmq.ui.AlarmQComposable
import srihk.alarmq.ui.InputDialog
import srihk.alarmq.ui.MINUTES_TEXT_FIELD_TAG

class UITest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun pressStart_getStop() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("srihk.alarmq", appContext.packageName)

        val snoozeList = mutableStateListOf<Int>()
        val state = mutableStateOf(0)
        val isRunning = mutableStateOf(false)
        val nextAlarm = mutableStateOf("")

        state.value = Preferences.getState(appContext)
        isRunning.value = Preferences.getIsRunning(appContext)
        nextAlarm.value = Preferences.getNextAlarm(appContext)

        rule.setContent {
            AlarmQComposable(
                modifier = Modifier.fillMaxSize(),
                snoozeList,
                isRunning = isRunning.value,
                state = state.value,
                nextAlarm = nextAlarm.value,
                updatePrefs = {
                    Preferences.setList(appContext, snoozeList)
                },
                onStart = {
                    isRunning.value = !isRunning.value
                }
            )
        }

        val startButton = rule.onNodeWithText("START")

        startButton.assertExists()
        startButton.performClick()
        rule.onNodeWithText("STOP").assertExists()
    }

    @Test
    fun enterNegativeNumber_getPositiveNumber() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("srihk.alarmq", appContext.packageName)

        val text = mutableStateOf("")

        rule.setContent {
            InputDialog(
                show = true,
                edit = true,
                text = text.value,
                enableAdd = true,
                onDismiss = {},
                onItemAdd = {},
                onValueChange = {
                    val itVal = it.toIntOrNull()
                    if (itVal != null && itVal >= 0 || it == "") {
                        text.value = it
                    }
                }
            )
        }

        rule.onNodeWithTag(MINUTES_TEXT_FIELD_TAG).assertExists()
        rule.onNodeWithTag(MINUTES_TEXT_FIELD_TAG).performTextInput("-")
        rule.onNodeWithTag(MINUTES_TEXT_FIELD_TAG).performTextInput("57")
        rule.onNodeWithTag(MINUTES_TEXT_FIELD_TAG).assert(hasText("57"))
    }
}