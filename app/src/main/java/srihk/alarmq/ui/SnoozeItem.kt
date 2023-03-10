package srihk.alarmq.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SnoozeItem(
    name: String,
    item: Int,
    showDelete: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    color: Color
) {
    Surface(color = color, modifier = Modifier.fillMaxWidth().clickable { onEdit() }) {
        Row(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#$name",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "$item minutes",
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )
                if (showDelete) {
                    Button(onClick = onDelete) {
                        Text(
                            text = "-"
                        )
                    }
                }
            }
        }
    }
}