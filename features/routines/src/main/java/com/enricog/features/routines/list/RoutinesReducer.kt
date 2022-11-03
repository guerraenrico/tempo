package com.enricog.features.routines.list

import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import javax.inject.Inject

internal class RoutinesReducer @Inject constructor() {

    fun setup(routines: List<Routine>): RoutinesState {
        return if (routines.isEmpty()) {
            RoutinesState.Empty
        } else {
            RoutinesState.Data(routines = routines, action = null)
        }
    }

    fun error(throwable: Throwable): RoutinesState {
        return RoutinesState.Error(throwable = throwable)
    }

    fun deleteRoutineError(state: RoutinesState.Data, routine: Routine): RoutinesState.Data {
        return state.copy(action = DeleteRoutineError(routine = routine))
    }

    fun actionHandled(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = null)
    }
}
