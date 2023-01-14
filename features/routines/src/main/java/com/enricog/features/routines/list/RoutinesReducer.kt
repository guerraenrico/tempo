package com.enricog.features.routines.list

import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineSuccess
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DuplicateRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import javax.inject.Inject

internal class RoutinesReducer @Inject constructor() {

    fun setup(state: RoutinesState, routines: List<Routine>): RoutinesState {
        return when {
            routines.isEmpty() -> RoutinesState.Empty
            state is RoutinesState.Data -> state.copy(routines = routines)
            else -> RoutinesState.Data(routines = routines, action = null)
        }
    }

    fun error(throwable: Throwable): RoutinesState {
        return RoutinesState.Error(throwable = throwable)
    }

    fun deleteRoutineError(state: RoutinesState.Data, routineId: ID): RoutinesState.Data {
        return state.copy(action = DeleteRoutineError(routineId = routineId))
    }

    fun deleteRoutineSuccess(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = DeleteRoutineSuccess)
    }

    fun moveRoutineError(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = MoveRoutineError)
    }

    fun duplicateRoutineError(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = DuplicateRoutineError)
    }

    fun actionHandled(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = null)
    }
}
