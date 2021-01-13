package com.enricog.routines.detail.routine.models

import com.enricog.entities.routines.Routine

internal sealed class RoutineState {
    object Idle : RoutineState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineField, RoutineFieldError>
    ) : RoutineState()
}