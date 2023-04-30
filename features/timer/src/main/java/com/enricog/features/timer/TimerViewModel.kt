package com.enricog.features.timer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
import com.enricog.features.timer.usecase.GetRoutineUseCase
import com.enricog.features.timer.usecase.GetTimerThemeUseCase
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: TimerStateConverter,
    private val timerController: TimerController,
    private val navigationActions: TimerNavigationActions,
    private val getRoutineUseCase: GetRoutineUseCase,
    private val getTimerThemeUseCase: GetTimerThemeUseCase,
    private val windowScreenManager: WindowScreenManager,
    @SuppressLint("StaticFieldLeak") @ApplicationContext private val context: Context
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter,
    dispatchers = dispatchers,
    configuration = ViewModelConfiguration(debounce = 0L)
) {

    private val input = TimerRoute.extractInput(savedStateHandle)
    private var loadJob by autoCancelableJob()

    init {
        timerController.state
            .onEach { state -> updateState { state } }
            .launchIn(viewModelScope)
        load(input)
    }

    private fun load(input: TimerRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading timer routine")
            updateState { TimerState.Error(throwable = throwable) }
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
        timerController.start(routine = routine, timerTheme = timerTheme)
        startForegroundService()
    }

    /**
     * TODO ask notification permissions
     */
    private fun startForegroundService() {
        Intent(context, TimerService::class.java).also { intent ->
//            intent.action = TimerServiceActions.START.name
            context.startForegroundService(intent)
        }
    }

    fun onPlay() {
        timerController.onPlay()
    }

    fun onToggleSound() {
        timerController.onToggleSound()
    }

    fun onBack() {
        timerController.onBack()
    }

    fun onNext() {
        timerController.onNext()
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
            navigationActions.backToRoutines()
        }
    }

    fun onRetryLoad() {
        load(input = input)
    }

    override fun onStateUpdated(currentState: TimerState) {
        toggleKeepScreenOn(currentState)
    }

    private fun toggleKeepScreenOn(currentState: TimerState) {
        val enableKeepScreenOn = currentState is TimerState.Counting &&
            currentState.isStepCountRunning && !currentState.isRoutineCompleted
        windowScreenManager.toggleKeepScreenOnFlag(enableKeepScreenOn)
    }

    override fun onCleared() {
        timerController.close()
    }
}
