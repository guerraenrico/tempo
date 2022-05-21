package com.enricog.features.routines.detail.segment.models

import com.enricog.data.routines.api.entities.TimeType

internal sealed class SegmentViewState {
    object Idle : SegmentViewState()

    data class Data(
        val segment: SegmentFields,
        val errors: Map<SegmentField, SegmentFieldError>,
        val timeTypes: List<TimeType>
    ) : SegmentViewState()
}
