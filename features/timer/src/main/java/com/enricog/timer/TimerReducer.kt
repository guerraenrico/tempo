package com.enricog.timer

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.Count
import com.enricog.timer.models.SegmentStep
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun setup(routine: Routine): TimerState {
        val segment = routine.segments.first()
        val (type, timeInSeconds) = getStepTypeAndTime(
            segment = segment,
            startTimeOffsetInSeconds = routine.startTimeOffsetInSeconds
        )
        return TimerState.Counting(
            routine = routine,
            runningSegment = routine.segments.first(),
            step = SegmentStep(
                count = Count.idle(timeInSeconds = timeInSeconds),
                type = type
            )
        )
    }

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting || !state.isCountRunning) return state

        val step = state.step
        val runningSegment = state.runningSegment
        val goal = when {
            step.type == SegmentStepType.STARTING -> 0L
            runningSegment.type == TimeType.STOPWATCH -> -1L
            else -> 0L
        }
        val progress = when (step.type) {
            SegmentStepType.STARTING -> -1L
            else -> runningSegment.type.progress
        }
        val timeInSeconds = step.count.timeInSeconds + progress
        val isCompleted = goal == timeInSeconds
        return state.copy(
            step = step.copy(
                count = step.count.copy(
                    timeInSeconds = timeInSeconds,
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
        val timeInSeconds = when (step.type) {
            SegmentStepType.STARTING -> routine.startTimeOffsetInSeconds
            SegmentStepType.IN_PROGRESS -> runningSegment.timeInSeconds
        }
        return state.copy(step = step.copy(count = Count.idle(timeInSeconds = timeInSeconds)))
    }

    fun nextStep(state: TimerState): TimerState {
        if (state !is TimerState.Counting || !state.isCountCompleted || state.isRoutineCompleted) return state

        val step = state.step
        val runningSegment = state.runningSegment

        return when (step.type) {
            SegmentStepType.STARTING -> {
                state.copy(
                    step = SegmentStep(
                        count = Count.start(runningSegment.timeInSeconds),
                        type = SegmentStepType.IN_PROGRESS
                    )
                )
            }
            SegmentStepType.IN_PROGRESS -> {
                val routine = state.routine
                val indexRunningSegment = routine.segments.indexOf(runningSegment)
                val segment = routine.segments[indexRunningSegment + 1]
                val (type, timeInSeconds) = getStepTypeAndTime(
                    segment = segment,
                    startTimeOffsetInSeconds = routine.startTimeOffsetInSeconds
                )
                state.copy(
                    runningSegment = segment,
                    step = SegmentStep(
                        count = Count.start(timeInSeconds = timeInSeconds),
                        type = type
                    )
                )
            }
        }
    }

    private fun getStepTypeAndTime(
        segment: Segment,
        startTimeOffsetInSeconds: Long
    ): Pair<SegmentStepType, Long> {
        return if (segment.type != TimeType.REST && startTimeOffsetInSeconds > 0) {
            SegmentStepType.STARTING to startTimeOffsetInSeconds
        } else {
            SegmentStepType.IN_PROGRESS to segment.timeInSeconds
        }
    }
}