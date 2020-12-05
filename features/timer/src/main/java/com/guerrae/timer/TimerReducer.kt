package com.guerrae.timer

import com.guerrae.timer.models.TimerState
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting) {
            return TimerState.Counting(1)
        }
        return state.copy(timeInSeconds = state.timeInSeconds + 1)
    }

}