package com.enricog.features.routines.detail.segment.models

import androidx.annotation.StringRes
import com.enricog.features.routines.R

internal enum class SegmentFieldError(@StringRes val stringResId: Int) {
    BlankSegmentName(stringResId = R.string.field_error_message_segment_name_blank),
    InvalidSegmentTime(stringResId = R.string.field_error_message_segment_time_invalid)
}
