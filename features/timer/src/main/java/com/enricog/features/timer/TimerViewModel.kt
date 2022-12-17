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
            updateState { reducer.setup(it, routine) }

            delay(ONE_SECOND)

            updateState { reducer.toggleTimeRunning(it) }
        }
    }

    fun onToggleTimer() {
        updateState { reducer.toggleTimeRunning(it) }
    }

    fun toggleSound() {
        updateState { reducer.toggleSound(it) }
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
            if (currentState.isStepCountRunning) {
                startCounting()
                return
            }
            if (currentState.isStepCountCompleted) {
                stopCounting()
                onCountCompleted()
                return
            }
        }
        stopCounting()
    }

    private fun onCountCompleted() {
        launch {
            delay(ONE_SECOND)
            updateState { reducer.nextStep(it) }
        }
    }

    private fun startCounting() {
        if (countingJob?.isActive == true) return

        countingJob = launch {
            while (true) {
                delay(ONE_SECOND)
                val newState = updateState { reducer.progressTime(it) }
                playSoundIfNeeded(state = newState)
            }
        }
    }

    private fun stopCounting() {
        countingJob?.cancel()
    }

    private fun playSoundIfNeeded(state: TimerState) {
        if (state !is TimerState.Counting || !state.isSoundEnabled) return

        when {
            state.isStepCountCompleting -> soundPlayer.play(COUNT_DOWN_SOUND)
            state.isStepCountCompleted -> soundPlayer.play(COUNT_DOWN_COMPLETED_SOUND)
        }
    }

    private fun toggleKeepScreenOn(currentState: TimerState) {
        val enableKeepScreenOn = currentState is TimerState.Counting &&
                currentState.isStepCountRunning && !currentState.isRoutineCompleted
        windowScreenManager.toggleKeepScreenOnFlag(enableKeepScreenOn)
    }

    override fun onCleared() {
        soundPlayer.close()
    }

    private companion object {
        const val ONE_SECOND = 1000L
        val COUNT_DOWN_SOUND = R.raw.sound_count_down
        val COUNT_DOWN_COMPLETED_SOUND = R.raw.sound_count_down_end
    }
}
