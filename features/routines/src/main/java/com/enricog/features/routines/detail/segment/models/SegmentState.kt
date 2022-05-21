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
        val timeTypes: List<TimeType>,
        val inputs: SegmentInputs
    ) : SegmentState()
}
