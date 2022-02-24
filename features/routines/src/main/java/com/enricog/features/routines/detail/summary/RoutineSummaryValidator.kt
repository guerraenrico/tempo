package com.enricog.features.routines.detail.summary

import com.enricog.entities.routines.Routine
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import javax.inject.Inject

internal class RoutineSummaryValidator @Inject constructor() {

    fun validate(routine: Routine): Map<RoutineSummaryField, RoutineSummaryFieldError> {
        return buildMap {
            if (routine.segments.isEmpty()) {
                put(RoutineSummaryField.Segments, RoutineSummaryFieldError.NoSegments)
            }
        }
    }
}
