package srihk.alarmq.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intervals")
data class IntervalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duration: Int,
    val orderIndex: Int,
    val ringtoneUri: String?
)