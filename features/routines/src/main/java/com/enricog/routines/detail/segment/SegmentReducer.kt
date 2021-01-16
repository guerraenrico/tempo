package com.enricog.routines.detail.segment

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.segment.models.SegmentField
import com.enricog.routines.detail.segment.models.SegmentFieldError
import com.enricog.routines.detail.segment.models.SegmentState
import javax.inject.Inject

internal class SegmentReducer @Inject constructor() {

    private val timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)

    private val newSegment: Segment = Segment(
        id = 0,
        name = "",
        timeInSeconds = 0,
        type = TimeType.TIMER
    )

    fun setup(routine: Routine, segmentId: Long?): SegmentState {
        val segment = segmentId?.let { id -> routine.segments.find { it.id == id } } ?: newSegment
        return SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = timeTypes
        )
    }

    fun updateSegmentName(state: SegmentState.Data, text: String): SegmentState.Data {
        val segment = state.segment.copy(name = text)
        val errors = state.errors.filterKeys { it != SegmentField.Name }
        return state.copy(
            segment = segment,
            errors = errors
        )
    }

    fun updateSegmentTime(state: SegmentState.Data, seconds: Long): SegmentState.Data {
        val timeInSeconds = if (state.segment.type == TimeType.STOPWATCH) {
            0L
        } else {
            seconds
        }

        val segment = state.segment.copy(timeInSeconds = timeInSeconds)
        val errors = state.errors.filterKeys { it != SegmentField.TimeInSeconds }
        return state.copy(
            segment = segment,
            errors = errors
        )
    }

    fun updateSegmentTimeType(state: SegmentState.Data, timeType: TimeType): SegmentState.Data {
        val timeInSeconds = if (timeType == TimeType.STOPWATCH) {
            0L
        } else {
            state.segment.timeInSeconds
        }

        val segment = state.segment.copy(
            type = timeType,
            timeInSeconds = timeInSeconds
        )
        return state.copy(segment = segment)
    }

    fun applySegmentErrors(
        state: SegmentState.Data,
        errors: Map<SegmentField, SegmentFieldError>
    ): SegmentState.Data {
        return state.copy(errors = errors)
    }
}