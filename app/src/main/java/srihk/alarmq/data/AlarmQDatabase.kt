package srihk.alarmq.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AlarmQStateEntity::class, IntervalEntity::class, Settings::class],
    version = 5
)
abstract class AlarmQDatabase : RoomDatabase() {
    abstract fun alarmQDao(): AlarmQDao
}