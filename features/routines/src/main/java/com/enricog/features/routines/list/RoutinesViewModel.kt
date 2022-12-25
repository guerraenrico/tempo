package com.enricog.features.routines.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.entities.ID
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.MoveRoutineUseCase
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

@Immutable
@HiltViewModel
internal class RoutinesViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutinesStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutinesReducer,
    private val routinesUseCase: RoutinesUseCase,
    private val moveRoutineUseCase: MoveRoutineUseCase
) : BaseViewModel<RoutinesState, RoutinesViewState>(
    initialState = RoutinesState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    private var loadJob by autoCancelableJob()
    private var deleteJob by autoCancelableJob()
    private var moveJob by autoCancelableJob()

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

    fun onRoutine(routineId: ID) {
        launch {
            navigationActions.goToRoutineSummary(routineId = routineId)
        }
    }

    fun onRoutineDelete(routineId: ID) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutinesState.Data> {
                reducer.deleteRoutineError(state = it, routineId = routineId)
            }
        }
        deleteJob = launchWhen<RoutinesState.Data>(exceptionHandler) { state ->
            val routine = state.routines.first { it.id == routineId }
            routinesUseCase.delete(routine)
        }
    }

    fun onRoutineMoved(draggedRoutineId: ID, hoveredRoutineId: ID?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutinesState.Data> { reducer.moveRoutineError(state = it) }
        }
        moveJob = launchWhen<RoutinesState.Data>(exceptionHandler) {
            moveRoutineUseCase(
                routines = it.routines,
                draggedRoutineId = draggedRoutineId,
                hoveredRoutineId = hoveredRoutineId
            )
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
                    is DeleteRoutineError -> onRoutineDelete(routineId = previousAction.routineId)
                    MoveRoutineError,
                    null -> Unit
                }
            }
        }
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
