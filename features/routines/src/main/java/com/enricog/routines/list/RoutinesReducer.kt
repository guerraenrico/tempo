package com.enricog.routines.list

import com.enricog.entities.routines.Routine
import com.enricog.routines.list.models.RoutinesState
import javax.inject.Inject

internal class RoutinesReducer @Inject constructor() {

    fun setup(routines: List<Routine>): RoutinesState {
        return if (routines.isEmpty()) {
            RoutinesState.Empty
        } else {
            RoutinesState.Data(routines = routines)
        }
    }
}
