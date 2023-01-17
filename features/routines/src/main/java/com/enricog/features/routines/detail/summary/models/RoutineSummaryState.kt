package com.enricog.features.routines.detail.summary.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID

internal sealed class RoutineSummaryState {

    object Idle : RoutineSummaryState()

    data class Data(
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
