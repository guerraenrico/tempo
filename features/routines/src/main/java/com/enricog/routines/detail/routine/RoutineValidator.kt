package com.enricog.routines.detail.routine

import com.enricog.entities.routines.Routine
import com.enricog.entities.seconds
import com.enricog.routines.detail.routine.models.RoutineField
import com.enricog.routines.detail.routine.models.RoutineFieldError
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
