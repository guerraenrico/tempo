package com.enricog.features.routines.detail.segment.models

import androidx.annotation.StringRes
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.features.routines.detail.ui.time_type.TimeType

internal sealed class SegmentViewState {

    object Idle : SegmentViewState()

    data class Data(
        val isTimeFieldVisible: Boolean,
        val errors: ImmutableMap<SegmentField, SegmentFieldError>,
        val selectedTimeType: TimeType,
        val timeTypes: ImmutableList<TimeType>,
        val message: Message?
    ) : SegmentViewState() {

        data class Message(
            @StringRes val textResId: Int,
            @StringRes val actionTextResId: Int
        )
    }

    data class Error(val throwable: Throwable) : SegmentViewState()
}
