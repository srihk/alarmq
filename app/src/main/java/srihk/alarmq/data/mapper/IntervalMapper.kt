package srihk.alarmq.data.mapper

import srihk.alarmq.data.Interval
import srihk.alarmq.data.IntervalEntity

fun IntervalEntity.toDomain(): Interval {
    return Interval(
        id = id,
        duration = duration,
        order = orderIndex
    )
}

fun Interval.toEntity(): IntervalEntity {
    return IntervalEntity(
        id = id,
        duration = duration,
        orderIndex = order
    )
}