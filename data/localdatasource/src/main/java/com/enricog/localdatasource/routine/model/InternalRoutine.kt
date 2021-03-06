package com.enricog.localdatasource.routine.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.entities.routines.Routine
import com.enricog.entities.seconds
import java.time.OffsetDateTime

@Entity(tableName = "Routines")
internal data class InternalRoutine(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "routineId") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "startTimeOffsetInSeconds") val startTimeOffsetInSeconds: Long,
    @ColumnInfo(name = "createdAt") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updatedAt") val updatedAt: OffsetDateTime
) {
    fun toEntity(segments: List<InternalSegment>): Routine {
        return Routine(
            id = id,
            name = name,
            startTimeOffset = startTimeOffsetInSeconds.seconds,
            createdAt = createdAt,
            updatedAt = updatedAt,
            segments = segments.map { it.toEntity() }
        )
    }
}

internal fun Routine.toInternal(): InternalRoutine {
    return InternalRoutine(
        id = id,
        name = name,
        startTimeOffsetInSeconds = startTimeOffset.value,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
