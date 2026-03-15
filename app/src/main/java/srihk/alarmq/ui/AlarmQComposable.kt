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

@Composable
fun AlarmQComposable(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    viewModel: AlarmQViewModel = viewModel(factory = AlarmQViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                itemsIndexed(items = uiState.intervalQueueContents) { index, item ->
                    SnoozeItem(
                        name = index.toString(),
                        item = item,
                        showDelete = !uiState.isActive,
                        onDelete = {
                            viewModel.deleteIntervalAtIndex(index)
                        },
                        onEdit = {
                            if (!uiState.isActive) {
                                text.value = uiState.intervalQueueContents[index].toString()
                                show.value = true
                                edit.value = true
                                editIndex.value = index
                            }
                        },
                        color = if (uiState.isActive && index == uiState.currentInterval) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.background
                        }
                    )
                }
                if (!uiState.isActive) {
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
            if (uiState.isActive) {
                Text("Next Alarm: ${uiState.nextAlarmScheduledTime}")
            }
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                val startStopButtonText = if (uiState.isActive) {
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
                viewModel.editInterval(editIndex.value, text.value.toInt())
                edit.value = false
            } else {
                viewModel.addInterval(text.value.toInt())
            }
            enableAdd.value = false
        },
        onValueChange = {
            val itVal = it.toIntOrNull()
            if (itVal != null && itVal >= 0 || it == "") {
                text.value = it
            }

            enableAdd.value = !(text.value == "" || text.value.toInt() == 0)
        })
}