package com.enricog.features.routines.detail.summary.models

import com.enricog.core.entities.ID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.timer.api.theme.entities.TimerTheme

internal sealed class RoutineSummaryState {

    object Idle : RoutineSummaryState()

    data class Data(
        val timerTheme: TimerTheme,
        val routine: Routine,
        val errors: Map<RoutineSummaryField, RoutineSummaryFieldError>,
        val action: Action?
    ) : RoutineSummaryState() {
        sealed class Action {
            data class DeleteSegmentError(val segmentId: ID) : Action()
            object DeleteSegmentSuccess : Action()
            object MoveSegmentError : Action()
            object DuplicateSegmentError : Action()
        }
    }

    data class Error(val throwable: Throwable) : RoutineSummaryState()
}
