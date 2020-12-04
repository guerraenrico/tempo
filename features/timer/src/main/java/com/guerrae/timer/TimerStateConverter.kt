package com.guerrae.timer

import com.guerrae.base_android.viewmodel.StateConverter
import com.guerrae.timer.models.TimerState
import com.guerrae.timer.models.TimerViewState
import javax.inject.Inject

internal class TimerStateConverter @Inject constructor() : StateConverter<TimerState, TimerViewState> {

    override suspend fun convert(state: TimerState): TimerViewState {
        return when (state) {
            TimerState.Idle -> TimerViewState.Idle
        }
    }

}