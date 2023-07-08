package com.enricog.features.routines.detail.routine.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.seconds
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText

internal data class RoutineInputs(
    val name: TextFieldValue,
    val preparationTime: TimeText,
    val rounds: TextFieldValue,
) {

    fun mergeToRoutine(routine: Routine): Routine {
        return routine.copy(
            name = name.text,
            preparationTime = preparationTime.toSeconds(),
            rounds =  rounds.text.toInt()
        )
    }

    companion object {
        val empty = RoutineInputs(
            name = "".toTextFieldValue(),
            preparationTime = TimeText.from(0.seconds),
            rounds = "1".toTextFieldValue()
        )
    }
}