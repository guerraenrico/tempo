package com.enricog.features.timer

import androidx.annotation.RestrictTo
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.async.awaitAll
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.api.statistics.entities.Statistic.Type.ROUTINE_ABORTED
import com.enricog.data.routines.api.statistics.entities.Statistic.Type.ROUTINE_COMPLETED
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.service.TimerServiceHandler
import com.enricog.features.timer.usecase.GetRoutineUseCase
import com.enricog.features.timer.usecase.GetTimerSettingsUseCase
import com.enricog.features.timer.usecase.GetTimerThemeUseCase
import com.enricog.features.timer.usecase.SaveStatisticUseCase
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Clock
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
    private val getTimerSettingsUseCase: GetTimerSettingsUseCase,
    private val saveStatisticUseCase: SaveStatisticUseCase,
    private val windowScreenManager: WindowScreenManager,
    private val serviceHandler: TimerServiceHandler,
    private val clock: Clock
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
        getTimerSettingsUseCase()
            .onEach { timerSettings -> timerController.onTimerSettingsChanged(timerSettings = timerSettings) }
            .launchIn(viewModelScope)
        onStateUpdate(::onStateUpdated)

        load(input)
    }

    private fun load(input: TimerRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading timer routine")
            updateState { TimerState.Error(throwable = throwable) }
        }
        loadJob = launch(exceptionHandler = exceptionHandler) {
            val (timerTheme, timerSettings, routine) = awaitAll(
                async { getTimerThemeUseCase() },
                async { getTimerSettingsUseCase().first() },
                async { getRoutineUseCase(routineId = input.routineId) }
            )
            start(routine = routine, timerTheme = timerTheme, timerSettings = timerSettings)
        }
    }

    private fun start(routine: Routine, timerTheme: TimerTheme, timerSettings: TimerSettings) {
        timerController.start(routine = routine, timerTheme = timerTheme, timerSettings = timerSettings)
    }

    fun onPlay() {
        timerController.onPlay()
    }

    fun onBack() {
        timerController.onBack()
    }

    fun onNext() {
        timerController.onNext()
    }

    fun onReset() {
        runWhen<TimerState.Counting> { state ->
            start(routine = state.routine, timerTheme = state.timerTheme, timerSettings = state.timerSettings)
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

    fun onShowSettings() {
        launch {
            navigationActions.openTimerSettings()
        }
    }

    private fun onStateUpdated(currentState: TimerState) {
        saveRoutineStatisticIf(state = currentState, type = ROUTINE_COMPLETED) { state ->
            state.isRoutineCompleted
        }
        toggleKeepScreenOn(currentState = currentState)
        toggleBackgroundService(currentState = currentState)
    }

    private fun toggleKeepScreenOn(currentState: TimerState) {
        val enableKeepScreenOn = currentState is TimerState.Counting &&
            currentState.isStepCountRunning &&
            !currentState.isRoutineCompleted &&
            currentState.timerSettings.keepScreenOnEnabled
        windowScreenManager.toggleKeepScreenOnFlag(enableKeepScreenOn)
    }

    private fun toggleBackgroundService(currentState: TimerState) {
        if (currentState is TimerState.Counting && currentState.timerSettings.runInBackgroundEnabled) {
            serviceHandler.start()
        } else {
            serviceHandler.stop()
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public override fun onCleared() {
        saveRoutineStatisticIf(state = state, type = ROUTINE_ABORTED) { state ->
            !state.isRoutineCompleted
        }
        serviceHandler.stop()
        timerController.close()
    }

    private fun saveRoutineStatisticIf(
        state: TimerState,
        type: Statistic.Type,
        condition: (TimerState.Counting) -> Boolean
    ) {
        if (state !is TimerState.Counting) return

        if (condition(state)) {
            saveStatisticUseCase(
                routine = state.routine,
                type = type,
                effectiveTime = state.effectiveTotalSeconds(clock = clock)
            )
        }
    }
}
