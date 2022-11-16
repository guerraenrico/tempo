package com.enricog.features.timer

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerState.Counting.Companion.getSegmentStepFrom
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun setup(routine: Routine): TimerState {
        val segment = routine.segments.first()
        val segmentStep = getSegmentStepFrom(
            routine = routine,
            segment = segment
        )
        return TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = segmentStep
        )
    }

    fun error(throwable: Throwable): TimerState {
        return TimerState.Error(throwable = throwable)
    }

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting || !state.isCountRunning) return state

        val step = state.step
        val runningSegment = state.runningSegment
        val goal = when {
            step.type == SegmentStepType.STARTING -> 0.seconds
            runningSegment.type == TimeType.STOPWATCH -> (-1).seconds
            else -> 0.seconds
        }
        val progress = when (step.type) {
            SegmentStepType.STARTING -> (-1).seconds
            else -> runningSegment.type.progress
        }
        val seconds = step.count.seconds + progress
        val isCompleted = goal == seconds
        return state.copy(
            step = step.copy(
                count = step.count.copy(
                    seconds = seconds,
                    isCompleted = isCompleted
                )
            )
        )
    }

    fun toggleTimeRunning(state: TimerState): TimerState {
        if (state !is TimerState.Counting || state.isCountCompleted) return state

        val step = state.step
        return if (state.isStopwatchRunning) {
            state.copy(step = step.copy(count = step.count.copy(isCompleted = true)))
        } else {
            state.copy(step = step.copy(count = step.count.copy(isRunning = !step.count.isRunning)))
        }
    }

    fun restartTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        val step = state.step
        val routine = state.routine
        val runningSegment = state.runningSegment
        val time = when (step.type) {
            SegmentStepType.STARTING -> routine.startTimeOffset
            SegmentStepType.IN_PROGRESS -> runningSegment.time
        }
        return state.copy(step = step.copy(count = Count.idle(seconds = time)))
    }

    fun nextStep(state: TimerState): TimerState {
        if (state !is TimerState.Counting || !state.isCountCompleted || state.isRoutineCompleted) return state

        val step = state.step

        return when (step.type) {
            SegmentStepType.STARTING -> {
                state.copy(
                    step = SegmentStep(
                        count = Count.start(state.runningSegment.time),
                        type = SegmentStepType.IN_PROGRESS
                    )
                )
            }
            SegmentStepType.IN_PROGRESS -> {
                val runningSegment = requireNotNull(state.nextSegment)
                val nextSegmentStep = requireNotNull(state.nextSegmentStep)
                state.copy(
                    runningSegment = runningSegment,
                    step = nextSegmentStep.copy(
                        count = Count.start(nextSegmentStep.count.seconds)
                    )
                )
            }
        }
    }
}
