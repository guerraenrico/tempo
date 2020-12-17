package com.enricog.timer

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.Count
import com.enricog.timer.models.SegmentStep
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun setup(routine: Routine): TimerState {
        return TimerState.Counting(
            routine = routine,
            runningSegment = routine.segments.first(),
            step = SegmentStep(
                count = Count.start(routine.startTimeOffsetInSeconds),
                type = SegmentStepType.STARTING
            )
        )
    }

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        val runningSegment = state.runningSegment
        val goal = when (runningSegment.type) {
            TimeType.TIMER, TimeType.REST -> 0L
            TimeType.STOPWATCH -> runningSegment.timeInSeconds
        }

        val progress = when (runningSegment.type) {
            TimeType.TIMER, TimeType.REST -> -1L
            TimeType.STOPWATCH -> 1L
        }

        val timeInSeconds = state.step.count.timeInSeconds + progress

        return if (timeInSeconds == goal) {
            state.next()
        } else {
            state.updateTime(timeInSeconds)
        }
    }


    fun toggleTimeRunning(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        return state.toggleTimeRunning()
    }

    fun restartTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        return state.restartTime()
    }
}