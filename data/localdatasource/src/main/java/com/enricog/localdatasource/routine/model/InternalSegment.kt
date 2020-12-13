package com.enricog.localdatasource.routine.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

@Entity(tableName = "Segments")
internal data class InternalSegment(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "segmentId") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "timeInSeconds") val timeInSeconds: Int,
    @ColumnInfo(name = "type") val type: TimeType
) {
    fun toEntity(): Segment {
        return Segment(id = id, name = name, timeInSeconds = timeInSeconds, type = type)
    }
}

internal fun Segment.toInternal(): InternalSegment {
    return InternalSegment(id = id, name = name, timeInSeconds = timeInSeconds, type = type)
}