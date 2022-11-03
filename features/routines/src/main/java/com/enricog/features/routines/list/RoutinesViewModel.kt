package com.enricog.features.routines.list

import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.RoutinesUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarEvent.ActionPerformed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class RoutinesViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutinesStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutinesReducer,
    private val routinesUseCase: RoutinesUseCase
) : BaseViewModel<RoutinesState, RoutinesViewState>(
    initialState = RoutinesState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    private var loadJob by autoCancelableJob()
    private var deleteJob by autoCancelableJob()

    init {
        load()
    }

    private fun load() {
        loadJob = routinesUseCase.getAll()
            .onEach { routines -> updateState { reducer.setup(routines) } }
            .catch { throwable ->
                TempoLogger.e(throwable = throwable, message = "Error loading routines")
                updateState { reducer.error(throwable = throwable) }
            }
            .launchIn(viewModelScope)
    }

    fun onCreateRoutine() {
        launch {
            navigationActions.goToRoutine(routineId = ID.new())
        }
    }

    fun onRoutine(routine: Routine) {
        launch {
            navigationActions.goToRoutineSummary(routineId = routine.id)
        }
    }

    fun onRoutineDelete(routine: Routine) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutinesState.Data> {
                reducer.deleteRoutineError(state = it, routine = routine)
            }
        }
        deleteJob = launchWhen<RoutinesState.Data>(exceptionHandler) {
            routinesUseCase.delete(routine)
        }
    }

    fun onRetryLoad() {
        load()
    }

    fun onSnackbarEvent(snackbarEvent: TempoSnackbarEvent) {
        launchWhen<RoutinesState.Data> {
            val previousAction = it.action
            updateStateWhen(reducer::actionHandled)
            delay(SNACKBAR_ACTION_DELAY)
            if (snackbarEvent == ActionPerformed) {
                when (previousAction) {
                    is DeleteRoutineError -> onRoutineDelete(routine = previousAction.routine)
                    null -> Unit
                }
            }
        }
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
