package com.enricog.features.timer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.base.viewmodel.StateConverter
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TimeTypeColors
import java.time.Clock
import javax.inject.Inject

internal class TimerStateConverter @Inject constructor(
    private val clock: Clock
) : StateConverter<TimerState, TimerViewState> {

    override suspend fun convert(state: TimerState): TimerViewState {
        return when (state) {
            TimerState.Idle -> TimerViewState.Idle
            is TimerState.Error -> TimerViewState.Error(throwable = state.throwable)
            is TimerState.Counting -> mapCounting(state)
        }
    }

    private fun mapCounting(state: TimerState.Counting): TimerViewState {
        return if (state.isRoutineCompleted) {
            TimerViewState.Completed(
                effectiveTotalTime = state.effectiveTotalSeconds(clock).timeText,
                skipCount = state.skipCount
            )
        } else {
            TimerViewState.Counting(
                timeInSeconds = state.runningStep.count.seconds.value,
                stepTitleId = state.getStepTitleId(),
                segmentName = state.runningSegment.name,
                clockBackgroundColor = state.getClockBackgroundColor(),
                isSoundEnabled = state.isSoundEnabled,
                timerActions = state.getActions()
            )
        }
    }

    private fun TimerState.Counting.getClockBackgroundColor(): BackgroundColor {
        val nextSegmentStep = nextSegmentStep
        return when (runningStep.type) {
            SegmentStepType.PREPARATION -> BackgroundColor(
                background = runningStep.type.getColor(),
                ripple = when {
                    isStepCountCompleted -> runningSegment.type.getColor()
                    else -> null
                }
            )
            else -> BackgroundColor(
                background = runningSegment.type.getColor(),
                ripple = when {
                    isStepCountCompleted -> when {
                        nextSegmentStep?.type == SegmentStepType.PREPARATION ->
                            nextSegmentStep.type.getColor()
                        runningSegment.type == nextSegment?.type ->
                            Color(red = 255, green = 255, blue = 255, alpha = 30)
                        else ->
                            nextSegment?.type?.getColor()
                    }
                    else -> null
                }
            )
        }
    }

    private fun SegmentStepType.getColor(): Color {
        return when (this) {
            SegmentStepType.PREPARATION -> TimeTypeColors.STARTING
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
            runningStep.type == SegmentStepType.PREPARATION -> R.string.title_segment_step_type_preparation
            runningStep.type == SegmentStepType.IN_PROGRESS -> R.string.title_segment_step_type_in_progress
            else -> throw IllegalArgumentException("unhandled case")
        }
    }

    private fun TimerState.Counting.getActions(): TimerViewState.Counting.Actions {
        return TimerViewState.Counting.Actions(
            back = TimerViewState.Counting.Actions.Button(
                iconResId = R.drawable.ic_timer_back,
                contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                size = TempoIconButtonSize.Normal
            ),
            play = TimerViewState.Counting.Actions.Button(
                iconResId = if (runningStep.count.isRunning) R.drawable.ic_timer_stop else R.drawable.ic_timer_play,
                contentDescriptionResId = if (runningStep.count.isRunning) R.string.content_description_button_stop_routine_segment else R.string.content_description_button_start_routine_segment,
                size = if (runningSegment.type == TimeType.STOPWATCH) TempoIconButtonSize.Large else TempoIconButtonSize.Normal
            ),
            next = TimerViewState.Counting.Actions.Button(
                iconResId = R.drawable.ic_timer_next,
                contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                size = TempoIconButtonSize.Normal
            )
        )
    }
}
