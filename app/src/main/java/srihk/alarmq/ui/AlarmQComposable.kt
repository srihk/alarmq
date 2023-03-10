package srihk.alarmq.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AlarmQComposable(
    modifier: Modifier = Modifier,
    items: SnapshotStateList<String>,
    isRunning: Boolean,
    state: Int,
    updatePrefs: () -> Unit,
    onStart: () -> Unit
) {
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
                itemsIndexed(items = items) { index, item ->
                    SnoozeItem(
                        name = index.toString(),
                        item = item,
                        showDelete = !isRunning,
                        onDelete = {
                            items.removeAt(index)
                            updatePrefs()
                        },
                        onEdit = {
                            if (!isRunning) {
                                text.value = items[index]
                                show.value = true
                                edit.value = true
                                editIndex.value = index
                            }
                        },
                        color = if (isRunning && index == state) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.background
                        }
                    )
                }
                if (!isRunning) {
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
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                val startStopButtonText = if (isRunning) {
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
                items[editIndex.value] = text.value
                edit.value = false
            } else {
                items.add(text.value)
            }
            enableAdd.value = false
            updatePrefs()
        },
        onValueChange = {
            val itVal = it.toIntOrNull()
            enableAdd.value = true
            if (itVal != null && itVal >= 0  || it == "") {
                text.value = it
                if (it == "") {
                    enableAdd.value = false
                }
            } else {
                enableAdd.value = false
            }
        })
}