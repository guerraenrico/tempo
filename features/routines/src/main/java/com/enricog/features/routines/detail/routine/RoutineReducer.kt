package com.enricog.features.routines.detail.routine

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Routine.Companion.MAX_START_TIME_OFFSET
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText
import javax.inject.Inject

internal class RoutineReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineState {
        val inputs = RoutineInputs(
            name = routine.name.toTextFieldValue(),
            startTimeOffset = routine.startTimeOffset.timeText
        )
        return RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            inputs = inputs
        )
    }

    fun updateRoutineName(state: RoutineState.Data, textFieldValue: TextFieldValue): RoutineState.Data {
        val inputs = state.inputs.copy(name = textFieldValue)
        val errors = state.errors.filterKeys { it != RoutineField.Name }
        return state.copy(inputs = inputs, errors = errors)
    }

    fun updateRoutineStartTimeOffset(state: RoutineState.Data, text: TimeText): RoutineState.Data {
        if (text.toSeconds() > MAX_START_TIME_OFFSET) {
            return state
        }
        val inputs = state.inputs.copy(startTimeOffset = text)
        return state.copy(inputs = inputs)
    }

    fun applyRoutineErrors(
        state: RoutineState.Data,
        errors: Map<RoutineField, RoutineFieldError>
    ): RoutineState.Data {
        return state.copy(errors = errors)
    }
}
