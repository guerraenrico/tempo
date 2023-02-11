package com.enricog.features.routines.detail.segment.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType

internal sealed class SegmentState {

    object Idle : SegmentState()

    data class Data(
        val routine: Routine,
        val segment: Segment,
        val errors: Map<SegmentField, SegmentFieldError>,
        val selectedTimeType: TimeType,
        val timeTypes: List<TimeType>,
        val action: Action?
    ) : SegmentState() {
        sealed class Action {
            object SaveSegmentError : Action()
        }
    }

    data class Error(val throwable: Throwable) : SegmentState()
}
