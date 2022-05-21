package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import javax.inject.Inject

internal class SegmentValidator @Inject constructor() {

    fun validate(inputs: SegmentInputs): Map<SegmentField, SegmentFieldError> {
        return buildMap {
            if (inputs.name.isBlank()) {
                put(SegmentField.Name, SegmentFieldError.BlankSegmentName)
            }
            val seconds = inputs.time.toSeconds()
            if (seconds <= 0.seconds && inputs.type != TimeType.STOPWATCH) {
                put(SegmentField.TimeInSeconds, SegmentFieldError.InvalidSegmentTime)
            }
        }
    }
}
