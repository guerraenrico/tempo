package com.enricog.features.timer

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.Background
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.textField.timeText
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
        if (state.isRoutineCompleted) {
            return TimerViewState.Completed(
                effectiveTotalTime = state.effectiveTotalSeconds(clock).timeText,
                skipCount = state.skipCount
            )
        }

        val clockBackgroundResource = state.getBackgroundResource()

        return TimerViewState.Counting(
            timeInSeconds = state.runningStep.count.seconds.value,
            stepTitleId = state.getStepTitleId(),
            segmentName = state.runningSegment.name,
            routineRoundText = state.getRoutineRoundText(),
            segmentRoundText = state.getSegmentRoundText(),
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

    private fun TimerState.Counting.getRoutineRoundText(): TimerViewState.Counting.RoundText? {
        if (routine.rounds == 1) {
            return null
        }
        return TimerViewState.Counting.RoundText(
            labelId = R.string.label_routine_round,
            formatArgs = immutableListOf(runningStep.routineRound, routine.rounds)
        )
    }

    private fun TimerState.Counting.getSegmentRoundText(): TimerViewState.Counting.RoundText? {
        if (runningSegment.rounds == 1) {
            return null
        }
        return TimerViewState.Counting.RoundText(
            labelId = R.string.label_routine_segment_round,
            formatArgs = immutableListOf(runningStep.segmentRound, runningSegment.rounds)
        )
    }

    private data class ClockBackgroundResource(
        val backgroundResource: TimerTheme.Resource,
        val rippleResource: TimerTheme.Resource?
    ) {

        fun toViewBackground(): Background {
            return Background(
                background = backgroundResource.background.toComposeColor(),
                ripple = rippleResource?.background?.toComposeColor()
            )
        }

        fun onBackgroundColor(): Color {
            return backgroundResource.onBackground.toComposeColor()
        }

        private fun TimerTheme.Asset.toComposeColor(): Color {
            return when (this) {
                is TimerTheme.Asset.Color -> Color(argb)
            }
        }
    }
}
