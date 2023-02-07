package com.enricog.features.routines.detail.routine

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action.SaveRoutineError
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarEvent.ActionPerformed
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import javax.inject.Inject

@Immutable
@HiltViewModel
internal class RoutineViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: RoutineStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineReducer,
    private val validator: RoutineValidator,
    private val routineUseCase: RoutineUseCase
) : BaseViewModel<RoutineState, RoutineViewState>(
    initialState = RoutineState.Idle,
    converter = converter,
    dispatchers = dispatchers,
    configuration = ViewModelConfiguration(debounce = 0)
) {

    private val input = RoutineRoute.extractInput(savedStateHandle)
    private var saveRoutineJob by autoCancelableJob()

    var fieldInputs by mutableStateOf(RoutineInputs.empty)

        private set

    init {
        load(input = input)
    }

    private fun load(input: RoutineRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading routine")
            updateState { reducer.error(throwable = throwable) }
        }

        launch(exceptionHandler = exceptionHandler) {
            val routine = routineUseCase.get(routineId = input.routineId)
            fieldInputs = RoutineInputs(
                name = routine.name.toTextFieldValue(),
                startTimeOffset = routine.startTimeOffset.timeText
            )
            updateState { reducer.setup(routine = routine) }
        }
    }

    fun onRoutineNameTextChange(textFieldValue: TextFieldValue) {
        updateStateWhen<RoutineState.Data> {
            fieldInputs = fieldInputs.copy(name = textFieldValue)
            reducer.updateRoutineName(state = it)
        }
    }

    fun onRoutineStartTimeOffsetChange(text: TimeText) {
        if (text.toSeconds() <= Routine.MAX_START_TIME_OFFSET) {
            fieldInputs = fieldInputs.copy(startTimeOffset = text)
        }
    }

    fun onRoutineStartTimeInfo() {
        launch {
            navigationActions.openRoutineStartTimeInfo()
        }
    }

    fun onRoutineBack() {
        launch {
            navigationActions.goBack()
        }
    }

    fun onRoutineSave() {
        runWhen<RoutineState.Data> { stateData ->
            val errors = validator.validate(inputs = fieldInputs)
            if (errors.isEmpty()) {
                save(routine = fieldInputs.mergeToRoutine(routine = stateData.routine))
            } else {
                updateStateWhen<RoutineState.Data> {
                    reducer.applyRoutineErrors(state = it, errors = errors)
                }
            }
        }
    }

    private fun save(routine: Routine) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error saving routine")
            updateStateWhen(reducer::saveRoutineError)
        }
        saveRoutineJob = launch(exceptionHandler = exceptionHandler) {
            val routineId = routineUseCase.save(routine = routine)
            when {
                routine.isNew -> navigationActions.goToRoutineSummary(routineId = routineId)
                else -> navigationActions.goBack()
            }
        }
    }

    fun onRetryLoad() {
        load(input = input)
    }

    fun onSnackbarEvent(snackbarEvent: TempoSnackbarEvent) {
        launchWhen<RoutineState.Data> {
            val previousAction = it.action
            updateStateWhen(reducer::actionHandled)
            delay(SNACKBAR_ACTION_DELAY)
            if (snackbarEvent == ActionPerformed) {
                when (previousAction) {
                    SaveRoutineError -> onRoutineSave()
                    null -> Unit
                }
            }
        }
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
