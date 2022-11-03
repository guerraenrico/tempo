package com.enricog.features.routines.detail.summary

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.MoveSegmentError
import javax.inject.Inject

internal class RoutineSummaryReducer @Inject constructor() {

    fun setup(routine: Routine): RoutineSummaryState {
        return RoutineSummaryState.Data(routine = routine, errors = emptyMap(), action = null)
    }

    fun error(throwable: Throwable): RoutineSummaryState {
        return RoutineSummaryState.Error(throwable)
    }

    fun deleteSegmentError(
        state: RoutineSummaryState.Data,
        segment: Segment
    ): RoutineSummaryState.Data {
        return state.copy(action = DeleteSegmentError(segment = segment))
    }

    fun segmentMoveError(state: RoutineSummaryState.Data): RoutineSummaryState.Data {
        return state.copy(action = MoveSegmentError)
    }

    fun applyRoutineErrors(
        state: RoutineSummaryState.Data,
        errors: Map<RoutineSummaryField, RoutineSummaryFieldError>
    ): RoutineSummaryState.Data {
        return state.copy(errors = errors)
    }

    fun actionHandled(state: RoutineSummaryState.Data): RoutineSummaryState.Data {
        return state.copy(action = null)
    }
}
