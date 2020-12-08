package com.enricog.timer

import com.enricog.timer.models.TimerState
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) {
            return TimerState.Counting(timeInSeconds = 1, isRunning = true)
        }
        return state.copy(timeInSeconds = state.timeInSeconds + 1)
    }


    fun toggleTimeRunning(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        return state.copy(isRunning = !state.isRunning)
    }

    fun restartTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        return TimerState.Counting(timeInSeconds = 0, isRunning = false)
    }
}