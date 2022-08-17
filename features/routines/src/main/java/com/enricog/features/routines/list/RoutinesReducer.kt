package com.enricog.features.routines.list

import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.list.models.RoutinesState
import javax.inject.Inject

internal class RoutinesReducer @Inject constructor() {

    fun setup(routines: List<Routine>): RoutinesState {
        return if (routines.isEmpty()) {
            RoutinesState.Empty
        } else {
            RoutinesState.Data(routines = routines)
        }
    }

    fun error(throwable: Throwable): RoutinesState {
        return RoutinesState.Error(throwable = throwable)
    }
}
