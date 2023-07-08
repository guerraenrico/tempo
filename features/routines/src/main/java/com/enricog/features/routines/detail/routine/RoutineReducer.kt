package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action.SaveRoutineError
import javax.inject.Inject

internal class RoutineReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineState {
        return RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            action = null
        )
    }

    fun error(throwable: Throwable): RoutineState {
        return RoutineState.Error(throwable = throwable)
    }

    fun updateRoutineNameError(state: RoutineState.Data): RoutineState.Data {
        val errors = state.errors.filterKeys { it != RoutineField.Name }
        return state.copy(errors = errors)
    }

    fun updateRoutineRoundsError(state: RoutineState.Data): RoutineState.Data {
        val errors = state.errors.filterKeys { it != RoutineField.Rounds }
        return state.copy(errors = errors)
    }

    fun applyRoutineErrors(
        state: RoutineState.Data,
        errors: Map<RoutineField, RoutineFieldError>
    ): RoutineState.Data {
        return state.copy(errors = errors)
    }

    fun saveRoutineError(state: RoutineState.Data): RoutineState.Data {
        return state.copy(action = SaveRoutineError)
    }

    fun actionHandled(state: RoutineState.Data): RoutineState.Data {
        return state.copy(action = null)
    }
}
