package com.enricog.features.routines.detail.segment.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.components.textField.TimeText

internal data class SegmentFields(
    val name: TextFieldValue,
    val time: TimeText?,
    val type: TimeType,
)
