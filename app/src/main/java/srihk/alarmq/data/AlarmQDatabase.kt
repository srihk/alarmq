package srihk.alarmq.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AlarmQStateEntity::class, IntervalEntity::class],
    version = 1
)
abstract class AlarmQDatabase : RoomDatabase() {
    abstract fun alarmQDao(): AlarmQDao
}