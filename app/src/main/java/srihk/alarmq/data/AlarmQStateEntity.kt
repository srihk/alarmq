package srihk.alarmq.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_state")
data class AlarmQStateEntity(
    @PrimaryKey val id: Int = 0,
    val isActive: Boolean,
    val currentIntervalId: Int?,
    val nextAlarmTime: Long?
)