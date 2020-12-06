package com.enricog.timer

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import javax.inject.Inject

internal class TimerStateConverter @Inject constructor() :
    StateConverter<TimerState, TimerViewState> {

    override suspend fun convert(state: TimerState): TimerViewState {
        return when (state) {
            TimerState.Idle -> TimerViewState.Idle
            is TimerState.Counting -> TimerViewState.Counting(timeInSeconds = state.timeInSeconds)
        }
    }

}