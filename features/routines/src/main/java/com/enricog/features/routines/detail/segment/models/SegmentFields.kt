package com.enricog.features.routines.detail.segment.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.ui.components.textField.TimeText

internal data class SegmentFields(
    val name: TextFieldValue,
    val time: TimeText,
    val type: TimeType,
)
