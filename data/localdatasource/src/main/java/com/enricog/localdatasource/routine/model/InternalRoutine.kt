package com.enricog.localdatasource.routine.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.entities.routines.Routine
import java.time.OffsetDateTime

@Entity(tableName = "Routines")
internal data class InternalRoutine(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "routineId") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "startTimeOffsetInSeconds") val startTimeOffsetInSeconds: Int,
    @ColumnInfo(name = "createdAt") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updatedAt") val updatedAt: OffsetDateTime,
) {
    fun toEntity(segments: List<InternalSegment>): Routine {
        return Routine(
            id = id,
            name = name,
            startTimeOffsetInSeconds = startTimeOffsetInSeconds,
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
        startTimeOffsetInSeconds = startTimeOffsetInSeconds,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}