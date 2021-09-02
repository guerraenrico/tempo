package com.enricog.timer

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.entities.routines.Routine
import com.enricog.navigation.routes.TimerRoute
import com.enricog.navigation.routes.TimerRouteInput
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.service.RoutineRunner
import com.enricog.timer.service.TimerWorkerLauncher
import com.enricog.timer.usecase.TimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val navigationActions: TimerNavigationActions,
    private val routineRunner: RoutineRunner,
    private val timerUseCase: TimerUseCase,
    private val windowScreenManager: WindowScreenManager,
    private val timerWorkerLauncher: TimerWorkerLauncher
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers
), TimerActions {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var countingJob by autoCancelableJob()

    private var loadJob by autoCancelableJob()

    init {
        routineRunner.state
            .onEach { newState -> updateState { newState } }
            .launchIn(viewModelScope)

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
        routineRunner.start(routine)
    }

    override fun onStartStopButtonClick() {
        routineRunner.toggleTimeRunning()
    }

    override fun onRestartSegmentButtonClick() {
        routineRunner.restart()
    }

    override fun onResetButtonClick() = runWhen<TimerState.Counting> { state ->
        start(state.routine)
    }

    override fun onDoneButtonClick() = launch {
        navigationActions.backToRoutines()
    }

    override fun onStateUpdated(currentState: TimerState) {
        toggleKeepScreenOn(currentState)
    }

    override fun onCloseButtonClick() = launch {
        routineRunner.stop()
        navigationActions.backToRoutines()
    }

    override fun onAppInBackground() {
        timerWorkerLauncher.launch()
    }

    private fun toggleKeepScreenOn(currentState: TimerState) {
        val enableKeepScreenOn = currentState is TimerState.Counting &&
                currentState.isCountRunning && !currentState.isRoutineCompleted
        windowScreenManager.toggleKeepScreenOnFlag(enableKeepScreenOn)
    }
}
