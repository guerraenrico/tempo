package com.enricog.routines.detail.models

import com.enricog.entities.routines.Routine

internal sealed class RoutineState {
    object Idle : RoutineState()

    data class Data(
        val routine: Routine,
        val errors: Map<Field.Routine, ValidationError>,
        val editingSegment: EditingSegment
    ) : RoutineState()
}