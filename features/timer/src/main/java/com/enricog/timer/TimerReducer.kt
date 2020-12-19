package com.enricog.timer

import com.enricog.entities.routines.Routine
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
                count = Count.idle(timeInSeconds = routine.startTimeOffsetInSeconds),
                type = SegmentStepType.STARTING
            )
        )
    }

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting || !state.isCountRunning) return state
        return state.progressTime()
    }

    fun toggleTimeRunning(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        // TODO should check if the stop button is pressed when the segment is completed?

        return if (state.isStopwatchRunning) {
            state.completeCount()
        } else {
            state.toggleTimeRunning()
        }
    }

    fun restartTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state
        return state.restartTime()
    }

    fun nextStep(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        return if (!state.isLastSegment) {
            state.nextStep()
        } else {
            state
        }
    }
}