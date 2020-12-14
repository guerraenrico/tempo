package com.enricog.localdatasource.routine.model

import androidx.room.Embedded
import androidx.room.Relation
import com.enricog.entities.routines.Routine

internal data class InternalRoutineWithSegments(
    @Embedded val routine: InternalRoutine,
    @Relation(
        parentColumn = "routineId_fk",
        entityColumn = "segmentId"
    )
    val segments: List<InternalSegment>
) {
    fun toEntity(): Routine {
        return routine.toEntity(segments)
    }
}