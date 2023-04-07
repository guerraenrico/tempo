package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.ID
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentState.Data.Action.SaveSegmentError
import javax.inject.Inject

internal class SegmentReducer @Inject constructor() {

    private val timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)

    fun setup(routine: Routine, timerTheme: TimerTheme, segmentId: ID): SegmentState {
        val segment = routine.segments.find { it.id == segmentId }
            ?: Segment.create(routine.getNewSegmentRank())
        return SegmentState.Data(
            timerTheme = timerTheme,
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            selectedTimeType = segment.type,
            timeTypes = timeTypes,
            action = null
        )
    }

    fun error(throwable: Throwable): SegmentState {
        return SegmentState.Error(throwable = throwable)
    }

    fun updateSegmentName(state: SegmentState.Data): SegmentState.Data {
        val errors = state.errors.filterKeys { it != SegmentField.Name }
        return state.copy(errors = errors)
    }

    fun updateSegmentTime(state: SegmentState.Data): SegmentState.Data {
        val errors = state.errors.filterKeys { it != SegmentField.Time }
        return state.copy(errors = errors)
    }

    fun updateSegmentTimeType(state: SegmentState.Data, timeType: TimeType): SegmentState.Data {
        if (state.selectedTimeType == timeType) return state

        val errors = state.errors.filterKeys { it != SegmentField.Time }
        return state.copy(errors = errors, selectedTimeType = timeType)
    }

    fun applySegmentErrors(
        state: SegmentState.Data,
        errors: Map<SegmentField, SegmentFieldError>
    ): SegmentState.Data {
        return state.copy(errors = errors)
    }

    fun saveSegmentError(state: SegmentState.Data): SegmentState.Data {
        return state.copy(action = SaveSegmentError)
    }

    fun actionHandled(state: SegmentState.Data): SegmentState.Data {
        return state.copy(action = null)
    }
}
