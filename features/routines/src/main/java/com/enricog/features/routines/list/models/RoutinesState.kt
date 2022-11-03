package com.enricog.features.routines.list.models

import com.enricog.data.routines.api.entities.Routine

internal sealed class RoutinesState {

    object Idle : RoutinesState()

    object Empty : RoutinesState()

    data class Data(val routines: List<Routine>, val action: Action?) : RoutinesState() {
        sealed class Action {
            data class DeleteRoutineError(val routine: Routine) : Action()
        }
    }

    data class Error(val throwable: Throwable) : RoutinesState()
}
