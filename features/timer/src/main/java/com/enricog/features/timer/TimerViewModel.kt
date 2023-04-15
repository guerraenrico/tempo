package com.enricog.features.timer

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.async.awaitAll
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.service.TimerService
import com.enricog.features.timer.service.TimerServiceActions
import com.enricog.features.timer.usecase.GetRoutineUseCase
import com.enricog.features.timer.usecase.GetTimerThemeUseCase
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val navigationActions: TimerNavigationActions,
    private val reducer: TimerReducer,
    private val getRoutineUseCase: GetRoutineUseCase,
    private val getTimerThemeUseCase: GetTimerThemeUseCase,
    private val windowScreenManager: WindowScreenManager,
    private val soundPlayer: TimerSoundPlayer,
    @ApplicationContext private val context: Context
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers,
    configuration = ViewModelConfiguration(debounce = 0L)
) {

    private val input = TimerRoute.extractInput(savedStateHandle)
    private var countingJob by autoCancelableJob()
    private var loadJob by autoCancelableJob()
    private var startJob by autoCancelableJob()

    init {

        startForegroundService()

        load(input)
    }

    fun startForegroundService() {
        Intent(context, TimerService::class.java).also { intent ->
            intent.action = TimerServiceActions.START.name
            context.startForegroundService(intent)
        }
    }

    private fun load(input: TimerRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading timer routine")
            updateState { reducer.error(throwable = throwable) }
        }
        loadJob = launch(exceptionHandler = exceptionHandler) {
            val (timerTheme, routine) = awaitAll(
                async { getTimerThemeUseCase() },
                async { getRoutineUseCase(routineId = input.routineId) }
            )
            start(routine = routine, timerTheme = timerTheme)
        }
    }

    private fun start(routine: Routine, timerTheme: TimerTheme) {
        startJob = launch {
            updateState { reducer.setup(state = it, routine = routine, timerTheme = timerTheme) }

            delay(ONE_SECOND)

            val newState = updateState { reducer.toggleTimeRunning(it) }
            soundPlayer.playFrom(state = newState)
        }
    }

    fun onPlay() {
        updateState { reducer.toggleTimeRunning(it) }
    }

    fun onToggleSound() {
        updateState { reducer.toggleSound(it) }
    }

    fun onBack() {
        updateState { reducer.jumpStepBack(it) }
    }

    fun onNext() {
        updateState { reducer.jumpStepNext(it) }
    }

    fun onReset() {
        runWhen<TimerState.Counting> { state ->
            start(routine = state.routine, timerTheme = state.timerTheme)
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

        if (currentState !is TimerState.Counting) {
            stopCounting()
            return
        }

        when {
            currentState.isStepCountRunning -> {
                startCounting()
            }
            currentState.isStepCountCompleted -> {
                stopCounting()
                onStepCountCompleted()
            }
            else -> {
                stopCounting()
            }
        }
    }

    private fun onStepCountCompleted() {
        launch {
            delay(ONE_SECOND)
            val newState = updateState { reducer.nextStep(it) }
            soundPlayer.playFrom(state = newState)
        }
    }

    private fun startCounting() {
        if (countingJob?.isActive == true) return

        countingJob = launch {
            while (true) {
                delay(ONE_SECOND)
                val newState = updateState { reducer.progressTime(it) }
                soundPlayer.playFrom(state = newState)
            }
        }
    }

    private fun stopCounting() {
        countingJob?.cancel()
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
    }
}
