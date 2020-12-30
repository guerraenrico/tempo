package com.enricog.routines.detail.validation

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.models.Field
import com.enricog.routines.detail.models.ValidationError
import javax.inject.Inject

internal class RoutineValidator @Inject constructor() {

    fun validate(routine: Routine): Map<Field, ValidationError> {
        return emptyMap()
    }

    fun validate(segment: Segment): Map<Field, ValidationError> {
        return emptyMap()
    }

}