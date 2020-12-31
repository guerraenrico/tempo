package com.enricog.routines.detail

import com.enricog.entities.routines.Routine
import com.enricog.routines.detail.models.EditingSegment
import com.enricog.routines.detail.models.RoutineState
import javax.inject.Inject

internal class RoutineReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineState {
        return RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            editingSegment = EditingSegment.None
        )
    }


}