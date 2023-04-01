package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import javax.inject.Inject

internal class SegmentValidator @Inject constructor() {

    fun validate(inputs: SegmentInputs, selectedTimeType: TimeType): Map<SegmentField, SegmentFieldError> {
        return buildMap {
            if (inputs.name.text.isBlank()) {
                put(SegmentField.Name, SegmentFieldError.BlankSegmentName)
            }
            val seconds = inputs.time.toSeconds()
            if (seconds <= 0.seconds && selectedTimeType != TimeType.STOPWATCH) {
                put(SegmentField.Time, SegmentFieldError.InvalidSegmentTime)
            }
        }
    }
}
