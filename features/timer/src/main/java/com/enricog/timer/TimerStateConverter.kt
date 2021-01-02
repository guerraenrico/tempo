package com.enricog.timer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import com.enricog.ui_components.resources.*
import javax.inject.Inject

internal class TimerStateConverter @Inject constructor() :
    StateConverter<TimerState, TimerViewState> {

    override suspend fun convert(state: TimerState): TimerViewState {
        return when (state) {
            TimerState.Idle -> TimerViewState.Idle
            is TimerState.Counting -> mapCounting(state)
        }
    }

    private fun mapCounting(state: TimerState.Counting): TimerViewState.Counting {
        return TimerViewState.Counting(
            step = state.step,
            stepTitleId = state.getStepTitleId(),
            segmentName = state.runningSegment.name,
            clockBackgroundColor = state.getClockBackgroundColor(),
            isRoutineCompleted = state.isRoutineCompleted,
            enableKeepScreenOn = state.isCountRunning && !state.isRoutineCompleted
        )
    }

    private fun TimerState.Counting.getClockBackgroundColor(): Color {
        return when {
            step.type == SegmentStepType.STARTING -> TimeTypeColors.STARTING
            runningSegment.type == TimeType.REST -> TimeTypeColors.REST
            runningSegment.type == TimeType.TIMER -> TimeTypeColors.TIMER
            runningSegment.type == TimeType.STOPWATCH -> TimeTypeColors.STOPWATCH
            else -> throw IllegalArgumentException("unhandled case")
        }
    }

    @StringRes
    private fun TimerState.Counting.getStepTitleId(): Int {
        return when {
            runningSegment.type == TimeType.REST -> R.string.title_segment_time_type_rest
            step.type == SegmentStepType.STARTING -> R.string.title_segment_step_type_starting
            step.type == SegmentStepType.IN_PROGRESS -> R.string.title_segment_step_type_in_progress
            else -> throw IllegalArgumentException("unhandled case")
        }
    }
}