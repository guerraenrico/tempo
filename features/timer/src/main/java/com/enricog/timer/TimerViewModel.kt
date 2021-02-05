package com.enricog.timer

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.entities.routines.Routine
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.usecase.TimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
internal class TimerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val navigationActions: TimerNavigationActions,
    private val reducer: TimerReducer,
    private val timerUseCase: TimerUseCase,
    private val windowScreenManager: WindowScreenManager
) : BaseViewModel<TimerState, TimerViewState>(
        initialState = TimerState.Idle,
        converter = converter,
        dispatchers = dispatchers
    ),
    TimerActions {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var countingJob by autoCancelableJob()

    private var loadJob by autoCancelableJob()

    private var startJob by autoCancelableJob()

    fun load(configuration: TimerConfiguration) {
        loadJob = viewModelScope.launch {
            val routine = timerUseCase.get(configuration.routineId)
            start(routine)
        }
    }

    private fun start(routine: Routine) {
        startJob = viewModelScope.launch {
            state = reducer.setup(routine)

            delay(1000)

            state = reducer.toggleTimeRunning(state)
        }
    }

    override fun onStartStopButtonClick() {
        state = reducer.toggleTimeRunning(state)
    }

    override fun onRestartSegmentButtonClick() {
        state = reducer.restartTime(state)
    }

    override fun onResetButtonClick() = runWhen<TimerState.Counting> { state ->
        start(state.routine)
    }

    override fun onDoneButtonClick() {
        navigationActions.backToRoutines()
    }

    override fun onStateUpdated(currentState: TimerState) {
        toggleKeepScreenOn(currentState)
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

    override fun onCloseButtonClick() {
        stopCounting()
        navigationActions.backToRoutines()
    }

    private fun onCountCompleted() {
        viewModelScope.launch {
            delay(1000)
            state = reducer.nextStep(state)
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

    private fun toggleKeepScreenOn(currentState: TimerState) {
        val enableKeepScreenOn = currentState is TimerState.Counting &&
            currentState.isCountRunning && !currentState.isRoutineCompleted
        windowScreenManager.toggleKeepScreenOnFlag(enableKeepScreenOn)
    }
}
