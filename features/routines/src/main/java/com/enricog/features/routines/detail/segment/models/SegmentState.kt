package com.enricog.features.routines.detail.segment.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

internal sealed class SegmentState {
    object Idle : SegmentState()

    data class Data(
        val routine: Routine,
        val segment: Segment,
        val errors: Map<SegmentField, SegmentFieldError>,
        val timeTypes: List<TimeType>
    ) : SegmentState()
}
