package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import javax.inject.Inject

internal class RoutineValidator @Inject constructor() {

    fun validate(routine: Routine): Map<RoutineField, RoutineFieldError> {
        return buildMap {
            if (routine.name.isBlank()) {
                put(RoutineField.Name, RoutineFieldError.BlankRoutineName)
            }
        }
    }
}
