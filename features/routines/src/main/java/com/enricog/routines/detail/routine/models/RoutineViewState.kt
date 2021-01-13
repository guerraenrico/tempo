package com.enricog.routines.detail.routine.models

import com.enricog.entities.routines.Routine

internal sealed class RoutineViewState {
    object Idle : RoutineViewState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineField, Int>
    ) : RoutineViewState()
}