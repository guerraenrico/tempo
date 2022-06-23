package com.enricog.features.routines.detail.routine

import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import javax.inject.Inject

internal class RoutineValidator @Inject constructor() {

    fun validate(inputs: RoutineInputs): Map<RoutineField, RoutineFieldError> {
        return buildMap {
            if (inputs.name.text.isBlank()) {
                put(RoutineField.Name, RoutineFieldError.BlankRoutineName)
            }
        }
    }
}
