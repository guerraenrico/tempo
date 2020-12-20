package com.enricog.timer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.CoroutineDispatchers
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.usecase.RoutineUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class TimerViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val reducer: TimerReducer,
    private val routineUseCase: RoutineUseCase
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers
), TimerActions {

    private var countingJob: Job? = null

    fun load(configuration: TimerConfiguration) {
        viewModelScope.launch {
            val routine = routineUseCase.get(configuration.routineId)
            state = reducer.setup(routine)

            // Wait to render the state
            delay(1000)

            state = reducer.toggleTimeRunning(state)
        }
    }

    override fun onStartStopButtonClick() {
        state = reducer.toggleTimeRunning(state)
    }

    override fun onRestartButtonClick() {
        state = reducer.restartTime(state)
    }

    override fun onStateUpdated(currentState: TimerState) {
        if (currentState is TimerState.Counting) {
            if (currentState.isCountRunning) {
                startCounting()
                return
            }
            if (currentState.isCountCompleted) {
                stopCounting()
                onCountCompleted()
                return
            }
        }
        stopCounting()
    }

    private fun onCountCompleted() {
        viewModelScope.launch {
            delay(1000)

            state = reducer.nextStep(state)

            state = reducer.toggleTimeRunning(state)
        }
    }

    private fun startCounting() {
        if (countingJob?.isActive == true) return

        countingJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                state = reducer.progressTime(state)
            }
        }
    }

    private fun stopCounting() {
        countingJob?.cancel()
    }
}