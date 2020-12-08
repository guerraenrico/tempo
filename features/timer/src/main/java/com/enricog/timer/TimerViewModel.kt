package com.enricog.timer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.CoroutineDispatchers
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class TimerViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val reducer: TimerReducer
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers
), TimerActions {

    private var countingJob: Job? = null

    init {
        viewModelStateFlow
            .onEach { currentState ->
                when {
                    currentState is TimerState.Counting && currentState.isRunning -> startCounting()
                    else -> stopCounting()
                }
            }
            .launchIn(viewModelScope)

        state = reducer.progressTime(state)
    }


    private fun startCounting() {
        if (countingJob?.isActive == true) return

        countingJob = viewModelScope.launch {
            for (i in 1..1000) {
                delay(1000)
                state = reducer.progressTime(state)
            }
        }
    }

    private fun stopCounting() {
        countingJob?.cancel()
    }

    override fun onStartStopButtonClick() {
        state = reducer.toggleTimeRunning(state)
    }

    override fun onRestartButtonClick() {
        state = reducer.restartTime(state)
    }
}