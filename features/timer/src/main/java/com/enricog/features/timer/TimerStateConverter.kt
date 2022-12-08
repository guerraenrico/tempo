package com.enricog.features.timer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.base.viewmodel.StateConverter
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
import com.enricog.ui.theme.TimeTypeColors
import javax.inject.Inject

internal class TimerStateConverter @Inject constructor() :
    StateConverter<TimerState, TimerViewState> {

    override suspend fun convert(state: TimerState): TimerViewState {
        return when (state) {
            TimerState.Idle -> TimerViewState.Idle
            is TimerState.Error -> TimerViewState.Error(throwable = state.throwable)
            is TimerState.Counting -> mapCounting(state)
        }
    }

    private fun mapCounting(state: TimerState.Counting): TimerViewState {
        return if (state.isRoutineCompleted) {
            TimerViewState.Completed
        } else {
            TimerViewState.Counting(
                step = state.step,
                stepTitleId = state.getStepTitleId(),
                segmentName = state.runningSegment.name,
                clockBackgroundColor = state.getClockBackgroundColor()
            )
        }
    }

    private fun TimerState.Counting.getClockBackgroundColor(): BackgroundColor {
        val nextSegmentStep = nextSegmentStep
        return when (step.type) {
            SegmentStepType.STARTING -> BackgroundColor(
                foreground = step.type.getColor(),
                ripple = when {
                    isCountCompleted -> runningSegment.type.getColor()
                    else -> null
                }
            )
            else -> BackgroundColor(
                foreground = runningSegment.type.getColor(),
                ripple = when {
                    isCountCompleted -> if (nextSegmentStep?.type == SegmentStepType.STARTING)
                        nextSegmentStep.type.getColor() else nextSegment?.type?.getColor()
                    else -> null
                }
            )
        }
    }

    private fun SegmentStepType.getColor(): Color {
        return when (this) {
            SegmentStepType.STARTING -> TimeTypeColors.STARTING
            else -> throw IllegalArgumentException("unhandled case")
        }
    }

    private fun TimeType.getColor(): Color {
        return when (this) {
            TimeType.REST -> TimeTypeColors.REST
            TimeType.TIMER -> TimeTypeColors.TIMER
            TimeType.STOPWATCH -> TimeTypeColors.STOPWATCH
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
