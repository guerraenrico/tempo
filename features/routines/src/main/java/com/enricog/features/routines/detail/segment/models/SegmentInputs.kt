package com.enricog.features.routines.detail.segment.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.seconds
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText

internal data class SegmentInputs(
    val name: TextFieldValue,
    val time: TimeText
) {

    fun mergeToSegment(segment: Segment, type: TimeType): Segment {
        return segment.copy(
            name = name.text,
            time = time.toSeconds(),
            type = type
        )
    }

    companion object {
        val empty = SegmentInputs(
            name = "".toTextFieldValue(),
            time = TimeText.from(0.seconds)
        )
    }
}