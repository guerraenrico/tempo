package com.enricog.routines.list

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import javax.inject.Inject

internal class RoutinesStateConverter @Inject constructor() :
    StateConverter<RoutinesState, RoutinesViewState> {

    override suspend fun convert(state: RoutinesState): RoutinesViewState {
        return when (state) {
            RoutinesState.Idle -> RoutinesViewState.Idle
        }
    }
}