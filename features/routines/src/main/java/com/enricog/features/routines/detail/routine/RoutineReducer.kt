package com.enricog.features.routines.detail.routine

import com.enricog.entities.Seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Routine.Companion.MAX_START_TIME_OFFSET
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineState
import javax.inject.Inject

internal class RoutineReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineState {
        return RoutineState.Data(
            routine = routine,
            errors = emptyMap()
        )
    }

    fun updateRoutineName(state: RoutineState.Data, text: String): RoutineState.Data {
        val routine = state.routine.copy(name = text)
        val errors = state.errors.filterKeys { it != RoutineField.Name }
        return state.copy(routine = routine, errors = errors)
    }

    fun updateRoutineStartTimeOffset(state: RoutineState.Data, seconds: Seconds): RoutineState.Data {
        if (seconds > MAX_START_TIME_OFFSET) {
            return state
        }
        val routine = state.routine.copy(startTimeOffset = seconds)
        val errors = state.errors.filterKeys { it != RoutineField.StartTimeOffsetInSeconds }
        return state.copy(routine = routine, errors = errors)
    }

    fun applyRoutineErrors(
        state: RoutineState.Data,
        errors: Map<RoutineField, RoutineFieldError>
    ): RoutineState.Data {
        return state.copy(errors = errors)
    }
}
