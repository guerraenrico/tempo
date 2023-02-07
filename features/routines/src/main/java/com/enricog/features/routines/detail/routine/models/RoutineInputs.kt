package com.enricog.features.routines.detail.routine.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.seconds
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText

internal data class RoutineInputs(
    val name: TextFieldValue,
    val startTimeOffset: TimeText
) {

    fun mergeToRoutine(routine: Routine): Routine {
        return routine.copy(
            name = name.text,
            startTimeOffset = startTimeOffset.toSeconds()
        )
    }

    companion object {
        val empty = RoutineInputs(
            name = "".toTextFieldValue(),
            startTimeOffset = TimeText.from(0.seconds)
        )
    }
}