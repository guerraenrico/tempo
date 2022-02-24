package com.enricog.features.routines.detail.segment.models

import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

internal sealed class SegmentViewState {
    object Idle : SegmentViewState()

    data class Data(
        val segment: Segment,
        val errors: Map<SegmentField, Int>,
        val timeTypes: List<TimeType>
    ) : SegmentViewState()
}
