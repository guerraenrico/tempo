package com.enricog.routines.detail

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.EditingSegment
import com.enricog.routines.detail.models.Field
import com.enricog.routines.detail.models.RoutineState
import com.enricog.routines.detail.models.ValidationError
import javax.inject.Inject

internal class RoutineReducer @Inject constructor() {

    private val timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)

    fun setup(routine: Routine): RoutineState {
        return RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            editingSegment = EditingSegment.None
        )
    }

    fun updateRoutineName(state: RoutineState.Data, text: String): RoutineState.Data {
        val routine = state.routine.copy(name = text)
        val errors = state.errors.filterKeys { it != Field.Routine.Name }
        return state.copy(routine = routine, errors = errors)
    }

    fun updateRoutineStartTimeOffset(state: RoutineState.Data, seconds: Long): RoutineState.Data {
        val routine = state.routine.copy(startTimeOffsetInSeconds = seconds)
        val errors = state.errors.filterKeys { it != Field.Routine.StartTimeOffsetInSeconds }
        return state.copy(routine = routine, errors = errors)
    }

    fun editNewSegment(state: RoutineState.Data): RoutineState.Data {
        val editingSegment = EditingSegment.Data(
            segment = Segment(id = 0, name = "", timeInSeconds = 0, type = TimeType.TIMER),
            errors = emptyMap(),
            timeTypes = timeTypes,
            originalSegmentPosition = -1
        )
        return state.copy(editingSegment = editingSegment)
    }

    fun editSegment(state: RoutineState.Data, segment: Segment): RoutineState.Data {
        val editingSegment = EditingSegment.Data(
            segment = segment,
            errors = emptyMap(),
            timeTypes = timeTypes,
            originalSegmentPosition = state.routine.segments.indexOf(segment)
        )
        return state.copy(editingSegment = editingSegment)
    }

    fun deleteSegment(state: RoutineState.Data, segment: Segment): RoutineState.Data {
        val segments = state.routine.segments.filterNot { it == segment }
        return state.copy(
            routine = state.routine.copy(segments = segments)
        )
    }

    fun updateSegmentName(state: RoutineState.Data, text: String): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val segment = state.editingSegment.segment.copy(name = text)
        val errors = state.editingSegment.errors.filterKeys { it != Field.Segment.Name }
        return state.copy(
            editingSegment = state.editingSegment.copy(
                segment = segment,
                errors = errors
            )
        )
    }

    fun updateSegmentTime(state: RoutineState.Data, seconds: Long): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val timeInSeconds = if (state.editingSegment.segment.type == TimeType.STOPWATCH) {
            0L
        } else {
            seconds
        }

        val segment = state.editingSegment.segment.copy(timeInSeconds = timeInSeconds)
        val errors = state.editingSegment.errors.filterKeys { it != Field.Segment.TimeInSeconds }
        return state.copy(
            editingSegment = state.editingSegment.copy(
                segment = segment,
                errors = errors
            )
        )
    }

    fun updateSegmentTimeType(state: RoutineState.Data, timeType: TimeType): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val timeInSeconds = if (timeType == TimeType.STOPWATCH) {
            0L
        } else {
            state.editingSegment.segment.timeInSeconds
        }

        val segment = state.editingSegment.segment.copy(
            type = timeType,
            timeInSeconds = timeInSeconds
        )
        return state.copy(editingSegment = state.editingSegment.copy(segment = segment))
    }

    fun updateRoutineSegment(state: RoutineState.Data): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val editedSegment = state.editingSegment.segment
        val segmentPosition = state.editingSegment.originalSegmentPosition
        val segments = if (segmentPosition >= 0) {
            state.routine.segments.mapIndexed { index, segment ->
                if (index == segmentPosition) {
                    editedSegment
                } else {
                    segment
                }
            }
        } else {
            buildList {
                addAll(state.routine.segments)
                add(editedSegment)
            }
        }

        val routine = state.routine.copy(segments = segments)
        val errors = state.errors.filterKeys { it != Field.Routine.Segments }
        return state.copy(
            routine = routine,
            errors = errors,
            editingSegment = EditingSegment.None
        )
    }

    fun closeEditSegment(state: RoutineState.Data): RoutineState.Data {
        return state.copy(editingSegment = EditingSegment.None)
    }

    fun applyRoutineErrors(
        state: RoutineState.Data,
        errors: Map<Field.Routine, ValidationError>
    ): RoutineState.Data {
        return state.copy(errors = errors)
    }

    fun applySegmentErrors(
        state: RoutineState.Data,
        errors: Map<Field.Segment, ValidationError>
    ): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state
        val editingSegment = state.editingSegment.copy(errors = errors)
        return state.copy(editingSegment = editingSegment)
    }
}