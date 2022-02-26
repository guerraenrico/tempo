package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import javax.inject.Inject

internal class SegmentValidator @Inject constructor() {

    fun validate(segment: Segment): Map<SegmentField, SegmentFieldError> {
        return buildMap {
            if (segment.name.isBlank()) {
                put(SegmentField.Name, SegmentFieldError.BlankSegmentName)
            }
            if (segment.time <= 0.seconds && segment.type != TimeType.STOPWATCH) {
                put(SegmentField.TimeInSeconds, SegmentFieldError.InvalidSegmentTime)
            }
        }
    }
}
