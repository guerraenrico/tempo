package com.enricog.routines.detail

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.detail.models.RoutineState
import com.enricog.routines.detail.models.RoutineViewState
import javax.inject.Inject

internal class RoutineStateConverter @Inject constructor() :
    StateConverter<RoutineState, RoutineViewState> {
    override suspend fun convert(state: RoutineState): RoutineViewState {
        return when (state) {
            RoutineState.Idle -> RoutineViewState.Idle
        }
    }
}