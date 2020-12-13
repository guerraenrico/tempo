package com.enricog.timer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.CoroutineDispatchers
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class TimerViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val reducer: TimerReducer,
    private val configuration: TimerConfiguration
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers
), TimerActions {

    private var countingJob: Job? = null

    init {
        state = reducer.progressTime(state)
    }

    override fun onStartStopButtonClick() {
        state = reducer.toggleTimeRunning(state)
    }

    override fun onRestartButtonClick() {
        state = reducer.restartTime(state)
    }

    override fun onStateUpdated(currentState: TimerState) {
        when {
            currentState is TimerState.Counting && currentState.isRunning && !currentState.isCompleted -> startCounting()
            else -> stopCounting()
        }
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
}