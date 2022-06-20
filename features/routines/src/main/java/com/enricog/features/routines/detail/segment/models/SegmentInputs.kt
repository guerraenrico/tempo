package com.enricog.features.routines.detail.segment.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.ui.components.textField.TimeText

internal data class SegmentInputs(
    val name: TextFieldValue,
    val time: TimeText,
    val type: TimeType
) {

    fun mergeToSegment(segment: Segment): Segment {
        return segment.copy(
            name = name.text,
            time = time.toSeconds(),
            type = type
        )
    }
}