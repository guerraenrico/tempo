package com.enricog.localdatasource.routine.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.enricog.entities.ID
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds

@Entity(
    tableName = "Segments",
    foreignKeys = [ForeignKey(
        entity = InternalRoutine::class,
        parentColumns = ["routineId"],
        childColumns = ["routineId_fk"],
        onDelete = CASCADE
    )]
)
internal data class InternalSegment(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "segmentId") val id: Long,
    @ColumnInfo(name = "routineId_fk") val routineId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "timeInSeconds") val timeInSeconds: Long,
    @ColumnInfo(name = "type") val type: TimeType
) {
    fun toEntity(): Segment {
        return Segment(id = ID.from(id), name = name, time = timeInSeconds.seconds, type = type)
    }
}

internal fun Segment.toInternal(routineId: Long): InternalSegment {
    return InternalSegment(
        id = id.toLong(),
        routineId = routineId,
        name = name,
        timeInSeconds = time.value,
        type = type
    )
}
