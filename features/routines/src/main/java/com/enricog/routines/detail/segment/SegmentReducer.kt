package com.enricog.routines.detail.segment

import com.enricog.entities.routines.TimeType
import javax.inject.Inject

internal class SegmentReducer @Inject constructor() {

    private val timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)

    fun updateSegmentName(state: RoutineState.Data, text: String): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val segment = state.editingSegment.segment.copy(name = text)
        val errors = state.editingSegment.errors.filterKeys { it != RoutineField.Segment.Name }
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
        val errors = state.editingSegment.errors.filterKeys { it != RoutineField.Segment.TimeInSeconds }
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
        val errors = state.errors.filterKeys { it != RoutineField.Routine.Segments }
        return state.copy(
            routine = routine,
            errors = errors,
            editingSegment = EditingSegment.None
        )
    }

    fun applySegmentErrors(
        state: RoutineState.Data,
        errors: Map<RoutineField.Segment, RoutineFieldError>
    ): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state
        val editingSegment = state.editingSegment.copy(errors = errors)
        return state.copy(editingSegment = editingSegment)
    }
}