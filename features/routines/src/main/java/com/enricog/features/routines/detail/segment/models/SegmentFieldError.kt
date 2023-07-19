package com.enricog.features.routines.detail.segment.models

import androidx.annotation.StringRes
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.R

internal enum class SegmentFieldError(@StringRes val stringResId: Int, val formatArgs: ImmutableList<Any>) {
    BlankSegmentName(
        stringResId = R.string.field_error_message_segment_name_blank,
        formatArgs = immutableListOf()
    ),
    BlankSegmentRounds(
        stringResId = R.string.field_error_message_segment_rounds_blank,
        formatArgs = immutableListOf(Segment.MIN_NUM_ROUNDS)
    ),
    InvalidSegmentTime(
        stringResId = R.string.field_error_message_segment_time_invalid,
        formatArgs = immutableListOf()
    )
}
