package com.enricog.features.routines.list

import com.enricog.base.viewmodel.StateConverter
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesViewState
import javax.inject.Inject

internal class RoutinesStateConverter @Inject constructor() :
    StateConverter<RoutinesState, RoutinesViewState> {

    override suspend fun convert(state: RoutinesState): RoutinesViewState {
        return when (state) {
            RoutinesState.Idle -> RoutinesViewState.Idle
            RoutinesState.Empty -> RoutinesViewState.Empty
            is RoutinesState.Data -> RoutinesViewState.Data(routines = state.routines)
        }
    }
}
