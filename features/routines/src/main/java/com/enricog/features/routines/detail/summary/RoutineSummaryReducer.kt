package com.enricog.features.routines.detail.summary

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import javax.inject.Inject

internal class RoutineSummaryReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineSummaryState {
        return RoutineSummaryState.Data(routine = routine, errors = emptyMap())
    }

    fun deleteSegment(state: RoutineSummaryState.Data, segment: Segment): RoutineSummaryState.Data {
        val segments = state.routine.segments.filterNot { it == segment }
        return state.copy(
            routine = state.routine.copy(segments = segments)
        )
    }

    fun applyRoutineErrors(
        state: RoutineSummaryState.Data,
        errors: Map<RoutineSummaryField, RoutineSummaryFieldError>
    ): RoutineSummaryState.Data {
        return state.copy(errors = errors)
    }
}
