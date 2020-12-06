package com.enricog.timer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.CoroutineDispatchers
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class TimerViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val reducer: TimerReducer
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    init {
        startCounting()
    }

    private fun startCounting() {
        viewModelScope.launch {
            for (i in 1..1000) {
                delay(200)
                state = reducer.progressTime(state)
            }
        }
    }

}