package com.enricog.routines.detail.validation

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.Field
import com.enricog.routines.detail.models.ValidationError
import javax.inject.Inject

internal class RoutineValidator @Inject constructor() {

    fun validate(routine: Routine): Map<Field.Routine, ValidationError> {
        return buildMap {
            if (routine.name.isBlank()) {
                put(Field.Routine.Name, ValidationError.BlankRoutineName)
            }
            if (routine.startTimeOffsetInSeconds < 0) {
                put(
                    Field.Routine.StartTimeOffsetInSeconds,
                    ValidationError.InvalidRoutineStartTimeOffset
                )
            }
            if (routine.segments.isEmpty()) {
                put(Field.Routine.Segments, ValidationError.NoSegmentsInRoutine)
            }
        }
    }

    fun validate(segment: Segment): Map<Field.Segment, ValidationError> {
        return buildMap {
            if (segment.name.isBlank()) {
                put(Field.Segment.Name, ValidationError.BlankSegmentName)
            }
            if (segment.timeInSeconds <= 0 && segment.type != TimeType.STOPWATCH) {
                put(Field.Segment.TimeInSeconds, ValidationError.InvalidSegmentTime)
            }
        }
    }

}