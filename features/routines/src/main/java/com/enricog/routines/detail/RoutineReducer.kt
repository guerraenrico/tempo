package com.enricog.routines.detail

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.EditingSegment
import com.enricog.routines.detail.models.RoutineState
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
        return state.copy(routine = routine)
    }

    fun updateRoutineStartTimeOffset(state: RoutineState.Data, seconds: Long): RoutineState.Data {
        val routine = state.routine.copy(startTimeOffsetInSeconds = seconds)
        return state.copy(routine = routine)
    }

    fun editNewSegment(state: RoutineState.Data): RoutineState.Data {
        val editingSegment = EditingSegment.Data(
            segment = Segment(id = 0, name = "", timeInSeconds = 0, type = TimeType.TIMER),
            errors = emptyMap(),
            timeTypes = timeTypes
        )
        return state.copy(editingSegment = editingSegment)
    }

    fun editSegment(state: RoutineState.Data, segment: Segment): RoutineState.Data {
        val editingSegment = EditingSegment.Data(
            segment = segment,
            errors = emptyMap(),
            timeTypes = timeTypes
        )
        return state.copy(editingSegment = editingSegment)
    }

    fun updateSegmentName(state: RoutineState.Data, text: String): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val segment = state.editingSegment.segment.copy(name = text)
        return state.copy(editingSegment = state.editingSegment.copy(segment = segment))
    }

    fun updateSegmentTime(state: RoutineState.Data, seconds: Long): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val segment = state.editingSegment.segment.copy(timeInSeconds = seconds)
        return state.copy(editingSegment = state.editingSegment.copy(segment = segment))
    }

    fun updateSegmentTimeType(state: RoutineState.Data, timeType: TimeType): RoutineState.Data {
        if (state.editingSegment !is EditingSegment.Data) return state

        val segment = state.editingSegment.segment.copy(type = timeType)
        return state.copy(editingSegment = state.editingSegment.copy(segment = segment))
    }

    fun closeEditSegment(state: RoutineState.Data): RoutineState.Data {
        return state.copy(editingSegment = EditingSegment.None)
    }

}