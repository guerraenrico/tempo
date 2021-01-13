package com.enricog.routines.detail.segment.models

import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

internal sealed class SegmentState {
    object Idle : SegmentState()

    data class Data(
        val segment: Segment,
        val errors: Map<SegmentField, SegmentFieldError>,
        val timeTypes: List<TimeType>
    ) : SegmentState()
}