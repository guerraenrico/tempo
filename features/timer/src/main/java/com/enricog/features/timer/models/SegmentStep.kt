package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds

internal data class SegmentStep(
    val count: Count,
    val type: SegmentStepType
) {
    companion object {
        fun from(routine: Routine, segment: Segment): SegmentStep {
            val (type, time) = if (segment.type != TimeType.REST && routine.startTimeOffset > 0.seconds) {
                SegmentStepType.STARTING to routine.startTimeOffset
            } else {
                SegmentStepType.IN_PROGRESS to segment.time
            }
            return SegmentStep(
                count = Count.idle(seconds = time),
                type = type
            )
        }
    }
}
