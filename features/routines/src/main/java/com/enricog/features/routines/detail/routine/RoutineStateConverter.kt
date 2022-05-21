package com.enricog.features.routines.detail.routine

import com.enricog.base.viewmodel.StateConverter
import com.enricog.features.routines.detail.routine.models.RoutineFields
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import javax.inject.Inject

internal class RoutineStateConverter @Inject constructor() :
    StateConverter<RoutineState, RoutineViewState> {

    override suspend fun convert(state: RoutineState): RoutineViewState {
        return when (state) {
            RoutineState.Idle -> RoutineViewState.Idle
            is RoutineState.Data -> state.toViewState()
        }
    }

    private fun RoutineState.Data.toViewState(): RoutineViewState {
        return RoutineViewState.Data(
            routine = RoutineFields(
                name = inputs.name,
                startTimeOffset = inputs.startTimeOffset
            ),
            errors = errors
        )
    }
}
