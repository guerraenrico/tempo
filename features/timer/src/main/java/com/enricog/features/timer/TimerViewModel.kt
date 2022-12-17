package com.enricog.features.timer

import androidx.lifecycle.SavedStateHandle
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.usecase.TimerUseCase
import com.enricog.libraries.sound.api.SoundPlayer
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val navigationActions: TimerNavigationActions,
    private val reducer: TimerReducer,
    private val timerUseCase: TimerUseCase,
    private val windowScreenManager: WindowScreenManager,
    private val soundPlayer: SoundPlayer
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    private val input = TimerRoute.extractInput(savedStateHandle)
    private var countingJob by autoCancelableJob()
    private var loadJob by autoCancelableJob()
    private var startJob by autoCancelableJob()

    init {
        load(input)
    }

    private fun load(input: TimerRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading timer routine")
            updateState { reducer.error(throwable = throwable) }
        }
        loadJob = launch(exceptionHandler = exceptionHandler) {
            val routine = timerUseCase.get(input.routineId)
            start(routine)
        }
    }

    private fun start(routine: Routine) {
        startJob = launch {
            updateState { reducer.setup(routine) }

            delay(1000)

            updateState { reducer.toggleTimeRunning(it) }
        }
    }

    fun onToggleTimer() {
        updateState { reducer.toggleTimeRunning(it) }
    }

    fun onRestartSegment() {
        updateState { reducer.restartTime(it) }
    }

    fun onReset() {
        runWhen<TimerState.Counting> { state ->
            start(state.routine)
        }
    }

    fun onDone() {
        launch {
            navigationActions.backToRoutines()
        }
    }

    fun onClose() {
        launch {
            stopCounting()
            navigationActions.backToRoutines()
        }
    }

    fun onRetryLoad() {
        load(input = input)
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

    private fun onCountCompleted() {
        launch {
            delay(1000)
            updateState { reducer.nextStep(it) }
        }
    }

    private fun startCounting() {
        if (countingJob?.isActive == true) return

        countingJob = launch {
            while (true) {
                delay(1000)
                updateState { reducer.progressTime(it) }
                soundPlayer.play(R.raw.sound_count_down)
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

    override fun onCleared() {
        soundPlayer.close()
    }
}
