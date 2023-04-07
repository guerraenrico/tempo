package com.enricog.features.routines.detail.summary

import com.enricog.core.entities.ID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentSuccess
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DuplicateSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.MoveSegmentError
import javax.inject.Inject

internal class RoutineSummaryReducer @Inject constructor() {

    fun setup(state: RoutineSummaryState, routine: Routine, timerTheme: TimerTheme): RoutineSummaryState {
        return when (state) {
            is RoutineSummaryState.Data -> state.copy(routine = routine)
            else -> RoutineSummaryState.Data(
                timerTheme = timerTheme,
                routine = routine,
                errors = emptyMap(),
                action = null
            )
        }
    }

    fun error(throwable: Throwable): RoutineSummaryState {
        return RoutineSummaryState.Error(throwable)
    }

    fun deleteSegmentError(
        state: RoutineSummaryState.Data,
        segmentId: ID
    ): RoutineSummaryState.Data {
        return state.copy(action = DeleteSegmentError(segmentId = segmentId))
    }

    fun deleteSegmentSuccess(state: RoutineSummaryState.Data): RoutineSummaryState.Data {
        return state.copy(action = DeleteSegmentSuccess)
    }

    fun moveSegmentError(state: RoutineSummaryState.Data): RoutineSummaryState.Data {
        return state.copy(action = MoveSegmentError)
    }

    fun duplicateSegmentError(state: RoutineSummaryState.Data): RoutineSummaryState.Data {
        return state.copy(action = DuplicateSegmentError)
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
