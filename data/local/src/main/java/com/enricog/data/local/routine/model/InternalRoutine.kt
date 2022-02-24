package com.enricog.data.local.routine.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.entities.ID
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.sortedByRank
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
            id = ID.from(id),
            name = name,
            startTimeOffset = startTimeOffsetInSeconds.seconds,
            createdAt = createdAt,
            updatedAt = updatedAt,
            segments = segments
                .map { it.toEntity() }
                .sortedByRank()
        )
    }
}

internal fun Routine.toInternal(): InternalRoutine {
    return InternalRoutine(
        id = id.toLong(),
        name = name,
        startTimeOffsetInSeconds = startTimeOffset.value,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
