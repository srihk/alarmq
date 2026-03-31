package srihk.alarmq.data

import android.net.Uri

data class Interval(
    val id: Int = 0,
    val duration: Int,
    val order: Int,
    val ringtoneUri: Uri?
)
