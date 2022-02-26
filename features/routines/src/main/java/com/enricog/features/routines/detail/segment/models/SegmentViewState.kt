package com.enricog.features.routines.detail.segment.models

import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType

internal sealed class SegmentViewState {
    object Idle : SegmentViewState()

    data class Data(
        val segment: Segment,
        val errors: Map<SegmentField, Int>,
        val timeTypes: List<TimeType>
    ) : SegmentViewState()
}
