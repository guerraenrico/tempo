package com.enricog.timer

import com.enricog.entities.Seconds
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.timer.models.Count
import com.enricog.timer.models.SegmentStep
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun setup(routine: Routine): TimerState {
        val segment = routine.segments.first()
        val (type, time) = getStepTypeAndTime(
            segment = segment,
            startTimeOffset = routine.startTimeOffset
        )
        return TimerState.Counting(
            routine = routine,
            runningSegment = routine.segments.first(),
            step = SegmentStep(
                count = Count.idle(seconds = time),
                type = type
            )
        )
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
        val runningSegment = state.runningSegment

        return when (step.type) {
            SegmentStepType.STARTING -> {
                state.copy(
                    step = SegmentStep(
                        count = Count.start(runningSegment.time),
                        type = SegmentStepType.IN_PROGRESS
                    )
                )
            }
            SegmentStepType.IN_PROGRESS -> {
                val routine = state.routine
                val indexRunningSegment = routine.segments.indexOf(runningSegment)
                val segment = routine.segments[indexRunningSegment + 1]
                val (type, time) = getStepTypeAndTime(
                    segment = segment,
                    startTimeOffset = routine.startTimeOffset
                )
                state.copy(
                    runningSegment = segment,
                    step = SegmentStep(
                        count = Count.start(seconds = time),
                        type = type
                    )
                )
            }
        }
    }

    private fun getStepTypeAndTime(
        segment: Segment,
        startTimeOffset: Seconds
    ): Pair<SegmentStepType, Seconds> {
        return if (segment.type != TimeType.REST && startTimeOffset > 0.seconds) {
            SegmentStepType.STARTING to startTimeOffset
        } else {
            SegmentStepType.IN_PROGRESS to segment.time
        }
    }
}
