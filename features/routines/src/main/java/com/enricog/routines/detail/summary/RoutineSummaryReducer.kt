package com.enricog.routines.detail.summary

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import javax.inject.Inject

internal class RoutineSummaryReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineSummaryState {
        return RoutineSummaryState.Data(routine = routine)
    }

    fun deleteSegment(state: RoutineSummaryState.Data, segment: Segment): RoutineSummaryState.Data {
        val segments = state.routine.segments.filterNot { it == segment }
        return state.copy(
            routine = state.routine.copy(segments = segments)
        )
    }
}