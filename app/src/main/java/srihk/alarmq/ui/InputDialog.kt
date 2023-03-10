package srihk.alarmq.ui

import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputDialog(
    show: Boolean,
    edit: Boolean,
    text: String,
    enableAdd: Boolean,
    onDismiss: () -> Unit,
    onValueChange: (it: String) -> Unit,
    onItemAdd: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text(text = "CANCEL")
                }
                Button(
                    enabled = enableAdd,
                    onClick = {
                        onItemAdd()
                        onDismiss()
                    }
                ) {
                    val updateText = if (edit) {
                        "EDIT"
                    } else {
                        "ADD"
                    }
                    Text(text = updateText)
                }
            },
            text = {
                OutlinedTextField(
                    value = text,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    label = { Text("Minutes") },
                    onValueChange = onValueChange
                )
            }
        )
    }
}