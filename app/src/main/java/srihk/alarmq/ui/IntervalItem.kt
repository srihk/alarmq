package srihk.alarmq.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import srihk.alarmq.data.Interval

@Composable
fun IntervalItem(
    itemIndex: Int,
    item: Interval,
    showDelete: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    color: Color,
    getRingtoneName: (Uri?) -> String
) {
    Surface(color = color, modifier = Modifier
        .fillMaxWidth()
        .clickable { onEdit() }) {
        Row(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${itemIndex}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "${item.duration} minutes"
                    )
                    if (item.ringtoneUri != null)
                        Text(
                            text = "Ringtone: ${getRingtoneName(item.ringtoneUri)}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                }
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