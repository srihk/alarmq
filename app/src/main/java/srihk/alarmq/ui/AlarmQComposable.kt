package srihk.alarmq.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import srihk.alarmq.data.Interval
import java.text.SimpleDateFormat

@Composable
fun AlarmQComposable(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    openRingtonePicker: (Interval) -> Unit,
    viewModel: AlarmQViewModel = viewModel(factory = AlarmQViewModel.Factory)
) {
    val alarmQState by viewModel.alarmQStateFlow.collectAsStateWithLifecycle()
    val intervalListState by viewModel.intervalListStateFlow.collectAsStateWithLifecycle()
    val show = rememberSaveable { mutableStateOf(false) }
    val edit = rememberSaveable { mutableStateOf(false) }
    val text = rememberSaveable { mutableStateOf("") }
    val editIndex = rememberSaveable { mutableStateOf(0) }
    val enableAdd = rememberSaveable {mutableStateOf(false) }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier) {
            LazyColumn(Modifier.weight(1f)) {
                itemsIndexed(items = intervalListState) { index, interval ->
                    IntervalItem(
                        item = interval,
                        showDelete = !alarmQState.isActive,
                        onDelete = {
                            viewModel.deleteInterval(interval)
                        },
                        onEdit = {
                            if (!alarmQState.isActive) {
                                text.value = intervalListState[index].duration.toString()
                                show.value = true
                                edit.value = true
                                editIndex.value = index
                            }
                        },
                        color = if (alarmQState.isActive && index == alarmQState.currentInterval) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.background
                        }
                    )
                }
                if (!alarmQState.isActive) {
                    item {
                        Button(
                            onClick = {
                                show.value = true
                                text.value = ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "+",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            if (alarmQState.isActive) {
                Text("Next Alarm: ${
                    SimpleDateFormat
                    .getDateTimeInstance()
                    .format(alarmQState.nextAlarmTime)
                }")
            }
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                val startStopButtonText = if (alarmQState.isActive) {
                    "STOP"
                } else {
                    "START"
                }
                Text(text = startStopButtonText, fontWeight = FontWeight.Bold)
            }
        }
    }

    InputDialog(
        show = show.value,
        edit = edit.value,
        text = text.value,
        enableAdd = enableAdd.value,
        onDismiss = {
            show.value = false
            edit.value = false
            enableAdd.value = false
            text.value = ""
        },
        onItemAdd = {
            if (edit.value) {
                viewModel.editInterval(intervalListState[editIndex.value].copy(
                    duration = text.value.toInt()
                ))
                edit.value = false
            } else {
                viewModel.addInterval(text.value.toInt())
            }
            enableAdd.value = false
        },
        openRingtonePicker = {
            openRingtonePicker(intervalListState[editIndex.value])
        },
        onValueChange = {
            val itVal = it.toIntOrNull()
            if (itVal != null && itVal >= 0 || it == "") {
                text.value = it
            }

            enableAdd.value = !(text.value == "" || text.value.toInt() == 0)
        })
}