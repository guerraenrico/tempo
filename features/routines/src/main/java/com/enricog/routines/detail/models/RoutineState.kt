package com.enricog.routines.detail.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment

internal sealed class RoutineState {
    object Idle : RoutineState()

    data class Data(
        val routine: Routine,
        val editingSegment: Segment?,
        val errors: Map<Field, ValidationError>
    ) : RoutineState()
}