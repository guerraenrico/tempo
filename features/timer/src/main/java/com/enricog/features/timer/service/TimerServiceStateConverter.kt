package com.enricog.features.timer.service

import android.graphics.Color
import androidx.annotation.StringRes
import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.entities.Seconds
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.timer.R
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import javax.inject.Inject

internal class TimerServiceStateConverter @Inject constructor() : StateConverter<TimerState, TimerServiceViewState> {

    override suspend fun convert(state: TimerState): TimerServiceViewState {
        return when (state) {
            is TimerState.Counting -> mapCounting(state = state)
            is TimerState.Error -> TimerServiceViewState.Error(throwable = state.throwable)
            TimerState.Idle -> TimerServiceViewState.Idle
        }
    }

    private fun mapCounting(state: TimerState.Counting): TimerServiceViewState {
        if (state.isRoutineCompleted) {
            return TimerServiceViewState.Completed
        }

        val clockBackgroundResource = state.getBackgroundResource()

        return TimerServiceViewState.Counting(
            time = state.runningStep.count.seconds.toText(),
            stepTitleId = state.getStepTitleId(),
            segmentName = state.runningSegment.name,
            clockBackground = clockBackgroundResource.toViewBackground(),
            clockOnBackgroundColor = clockBackgroundResource.onBackgroundColor(),
            timerActions = state.getActions()
        )
    }

    private fun TimerState.Counting.getBackgroundResource(): ClockBackgroundResource {
        val nextSegmentStep = nextSegmentStep
        return when (runningStep.type) {
            SegmentStepType.PREPARATION -> ClockBackgroundResource(
                backgroundResource = timerTheme.preparationTimeResource,
                rippleResource = when {
                    isStepCountCompleted -> runningSegment.type.getResource(timerTheme = timerTheme)
                    else -> null
                }
            )
            else -> ClockBackgroundResource(
                backgroundResource = runningSegment.type.getResource(timerTheme = timerTheme),
                rippleResource = when {
                    isStepCountCompleted -> when {
                        nextSegmentStep?.type == SegmentStepType.PREPARATION ->
                            timerTheme.preparationTimeResource
                        runningSegment.type == nextSegment?.type ->
                            TimerTheme.Resource(
                                background = TimerTheme.Asset.Color(argb = 2233785410880798720.toULong()),
                                onBackground = TimerTheme.Asset.Color(argb = 2233785410880798720.toULong())
                            )
                        else ->
                            nextSegment?.type?.getResource(timerTheme = timerTheme)
                    }
                    else -> null
                }
            )
        }
    }

    private fun TimeType.getResource(timerTheme: TimerTheme): TimerTheme.Resource {
        return when (this) {
            TimeType.REST -> timerTheme.restResource
            TimeType.TIMER -> timerTheme.timerResource
            TimeType.STOPWATCH -> timerTheme.stopwatchResource
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

    private fun TimerState.Counting.getActions(): TimerServiceViewState.Counting.Actions {
        return TimerServiceViewState.Counting.Actions(
            play = TimerServiceViewState.Counting.Actions.Button(
                iconResId = if (runningStep.count.isRunning) R.drawable.ic_timer_stop else R.drawable.ic_timer_play,
                contentDescriptionResId = if (runningStep.count.isRunning) R.string.content_description_button_stop_routine_segment else R.string.content_description_button_start_routine_segment
            )
        )
    }

    private fun Seconds.toText(): String {
        val minutes = value / 60
        val seconds = value - (minutes * 60)
        val format = "%02d"

        return buildString {
            append(String.format(format, minutes))
            append(":")
            append(String.format(format, seconds))
        }
    }

    private data class ClockBackgroundResource(
        val backgroundResource: TimerTheme.Resource,
        val rippleResource: TimerTheme.Resource?
    ) {

        fun toViewBackground(): TimerServiceViewState.Counting.Background {
            return TimerServiceViewState.Counting.Background(
                background = backgroundResource.background.toAndroidColor()
            )
        }

        fun onBackgroundColor(): Color {
            return backgroundResource.onBackground.toAndroidColor()
        }

        private fun TimerTheme.Asset.toAndroidColor(): Color {
            return when (this) {
                is TimerTheme.Asset.Color -> Color.valueOf(argb.toLong())
            }
        }
    }
}