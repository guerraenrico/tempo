package com.enricog.features.routines.detail.routine.models

import com.enricog.data.routines.api.entities.Routine

internal sealed class RoutineState {

    object Idle : RoutineState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineField, RoutineFieldError>,
        val action: Action?
    ) : RoutineState() {
        sealed class Action {
            object SaveRoutineError : Action()
        }
    }

    data class Error(val throwable: Throwable) : RoutineState()
}
