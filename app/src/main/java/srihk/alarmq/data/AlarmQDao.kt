package srihk.alarmq.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmQDao {
    @Query("SELECT * FROM intervals ORDER BY orderIndex")
    fun getIntervals(): Flow<List<IntervalEntity>>

    @Query("SELECT * FROM intervals ORDER BY orderIndex")
    suspend fun getIntervalsOnce(): List<IntervalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntervals(intervals: List<IntervalEntity>)

    @Query("""
        INSERT INTO intervals (duration, orderIndex)
        VALUES (
            :duration,
            (SELECT COALESCE(MAX(orderIndex), -1) + 1 FROM intervals)
        )
    """)
    suspend fun insertInterval(duration: Int)

    @Update
    suspend fun updateInterval(interval: IntervalEntity)

    @Delete
    suspend fun deleteInterval(interval: IntervalEntity)

    @Query("DELETE FROM intervals")
    suspend fun clearIntervals()

    @Query("SELECT * FROM alarm_state WHERE id = 0")
    fun getAlarmQState(): Flow<AlarmQStateEntity?>

    @Query("SELECT * FROM alarm_state WHERE id = 0")
    suspend fun getAlarmQStateOnce(): AlarmQStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAlarmQState(state: AlarmQStateEntity)
}