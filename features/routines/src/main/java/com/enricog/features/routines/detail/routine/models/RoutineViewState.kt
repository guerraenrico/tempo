package com.enricog.features.routines.detail.routine.models

internal sealed class RoutineViewState {
    object Idle : RoutineViewState()

    data class Data(
        val routine: RoutineFields,
        val errors: Map<RoutineField, RoutineFieldError>
    ) : RoutineViewState()
}
