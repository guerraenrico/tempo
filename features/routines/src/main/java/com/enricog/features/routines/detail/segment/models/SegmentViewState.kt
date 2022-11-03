package com.enricog.features.routines.detail.segment.models

import androidx.annotation.StringRes
import com.enricog.data.routines.api.entities.TimeType

internal sealed class SegmentViewState {
    object Idle : SegmentViewState()

    data class Data(
        val segment: SegmentFields,
        val errors: Map<SegmentField, SegmentFieldError>,
        val timeTypes: List<TimeType>,
        val message: Message?
    ) : SegmentViewState() {
        data class Message(@StringRes val textResId: Int, @StringRes val actionTextResId: Int)
    }

    data class Error(val throwable: Throwable) : SegmentViewState()
}
