package com.enricog.features.routines.detail.routine.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.ui.components.textField.TimeText

internal data class RoutineInputs(
    val name: String,
    val startTimeOffset: TimeText
) {

    fun mergeToRoutine(routine: Routine): Routine {
        return routine.copy(
            name = name,
            startTimeOffset = startTimeOffset.toSeconds()
        )
    }
}