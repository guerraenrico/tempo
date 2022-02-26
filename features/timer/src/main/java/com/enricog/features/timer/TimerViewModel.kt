package com.enricog.features.timer

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.timer.models.TimerActions
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.usecase.TimerUseCase
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
), TimerActions {

    private var countingJob by autoCancelableJob()
    private var loadJob by autoCancelableJob()
    private var startJob by autoCancelableJob()

    init {
        val input = TimerRoute.extractInput(savedStateHandle)
        load(input)
    }

    private fun load(input: TimerRouteInput) {
        loadJob = viewModelScope.launch {
            val routine = timerUseCase.get(input.routineId)
            start(routine)
        }
    }

    private fun start(routine: Routine) {
        startJob = viewModelScope.launch {
            updateState { reducer.setup(routine) }

            delay(1000)

            updateState { reducer.toggleTimeRunning(it) }
        }
    }

    override fun onStartStopButtonClick() {
        updateState { reducer.toggleTimeRunning(it) }
    }

    override fun onRestartSegmentButtonClick() {
        updateState { reducer.restartTime(it) }
    }

    override fun onResetButtonClick() = runWhen<TimerState.Counting> { state ->
        start(state.routine)
    }

    override fun onDoneButtonClick() = launch {
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

    override fun onCloseButtonClick() = launch {
        stopCounting()
        navigationActions.backToRoutines()
    }

    private fun onCountCompleted() {
        viewModelScope.launch {
            delay(1000)
            updateState { reducer.nextStep(it) }
        }
    }

    private fun startCounting() {
        if (countingJob?.isActive == true) return

        countingJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                updateState { reducer.progressTime(it) }
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
