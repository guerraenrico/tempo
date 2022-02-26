package com.enricog.data.local.routine.model

import androidx.room.Embedded
import androidx.room.Relation
import com.enricog.data.routines.api.entities.Routine

internal data class InternalRoutineWithSegments(
    @Embedded val routine: InternalRoutine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId_fk"
    )
    val segments: List<InternalSegment>
) {
    fun toEntity(): Routine {
        return routine.toEntity(segments)
    }
}
