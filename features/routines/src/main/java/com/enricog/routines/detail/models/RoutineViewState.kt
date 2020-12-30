package com.enricog.routines.detail.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment

internal sealed class RoutineViewState {
    object Idle : RoutineViewState()

    data class Data(
        val routine: Routine,
        val editingSegment: Segment?,
        val errors: List<FieldError>
    ) : RoutineViewState()

}