package com.enricog.features.routines.detail.segment.models

import com.enricog.data.routines.api.entities.TimeType
import com.enricog.ui.components.textField.TimeText

internal data class SegmentInputs(
    val name: String,
    val time: TimeText,
    val type: TimeType
)