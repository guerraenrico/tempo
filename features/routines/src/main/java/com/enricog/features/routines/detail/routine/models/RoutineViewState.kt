package com.enricog.features.routines.detail.routine.models

import com.enricog.data.routines.api.entities.Routine

internal sealed class RoutineViewState {
    object Idle : RoutineViewState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineField, Int>
    ) : RoutineViewState()
}
