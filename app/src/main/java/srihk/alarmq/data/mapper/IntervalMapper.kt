package srihk.alarmq.data.mapper

import androidx.core.net.toUri
import srihk.alarmq.data.Interval
import srihk.alarmq.data.IntervalEntity

fun IntervalEntity.toDomain(): Interval {
    return Interval(
        id = id,
        duration = duration,
        ringtoneUri = ringtoneUri?.toUri()
    )
}

fun Interval.toEntity(): IntervalEntity {
    return IntervalEntity(
        id = id,
        duration = duration,
        ringtoneUri = ringtoneUri?.toString()
    )
}