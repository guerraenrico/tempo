package com.enricog.routines.detail.segment

import com.enricog.entities.ID
import com.enricog.entities.Seconds
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.routines.detail.segment.models.SegmentField
import com.enricog.routines.detail.segment.models.SegmentFieldError
import com.enricog.routines.detail.segment.models.SegmentState
import javax.inject.Inject

internal class SegmentReducer @Inject constructor() {

    private val timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)

    fun setup(routine: Routine, segmentId: ID): SegmentState {
        val segment = routine.segments.find { it.id == segmentId }
            ?: Segment.create(routine.getNewSegmentRank())
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

    fun updateSegmentTime(state: SegmentState.Data, seconds: Seconds): SegmentState.Data {
        val time = if (state.segment.type == TimeType.STOPWATCH) {
            0.seconds
        } else {
            seconds
        }

        val segment = state.segment.copy(time = time)
        val errors = state.errors.filterKeys { it != SegmentField.TimeInSeconds }
        return state.copy(
            segment = segment,
            errors = errors
        )
    }

    fun updateSegmentTimeType(state: SegmentState.Data, timeType: TimeType): SegmentState.Data {
        val time = if (timeType == TimeType.STOPWATCH) {
            0.seconds
        } else {
            state.segment.time
        }

        val segment = state.segment.copy(
            type = timeType,
            time = time
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
