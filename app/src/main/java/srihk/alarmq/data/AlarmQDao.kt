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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntervals(intervals: List<IntervalEntity>)

    @Insert
    suspend fun insertInterval(interval: IntervalEntity)

    @Update
    suspend fun updateInterval(interval: IntervalEntity)

    @Delete
    suspend fun deleteInterval(interval: IntervalEntity)

    @Query("DELETE FROM intervals")
    suspend fun clearIntervals()

    @Query("SELECT * FROM alarm_state WHERE id = 0")
    fun getAlarmQState(): Flow<AlarmQStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAlarmQState(state: AlarmQStateEntity)
}