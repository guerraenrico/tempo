package com.enricog.features.routines.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineSuccess
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DuplicateRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.DuplicateRoutineUseCase
import com.enricog.features.routines.list.usecase.MoveRoutineUseCase
import com.enricog.features.routines.list.usecase.RoutinesUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarEvent.ActionPerformed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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
    private val moveRoutineUseCase: MoveRoutineUseCase,
    private val duplicateRoutineUseCase: DuplicateRoutineUseCase
) : BaseViewModel<RoutinesState, RoutinesViewState>(
    initialState = RoutinesState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    private var loadJob by autoCancelableJob()
    private var moveJob by autoCancelableJob()
    private var duplicateJob by autoCancelableJob()

    private val queueRoutineToDelete = MutableStateFlow<Routine?>(null)

    init {
        load()
    }

    private fun load() {
        loadJob = routinesUseCase.getAll()
            .combine(queueRoutineToDelete) { routines, routineToDelete -> routines.filter { it != routineToDelete } }
            .onEach { routines -> updateState { reducer.setup(state = it, routines = routines) } }
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
        setRoutineToDeleteQueue(routineId = routineId)
        updateStateWhen<RoutinesState.Data> {
            reducer.deleteRoutineSuccess(state = it)
        }
    }

    private fun setRoutineToDeleteQueue(routineId: ID) {
        launchWhen<RoutinesState.Data> { state ->
            runDeleteQueuedRoutine()
            val routine = state.routines.first { it.id == routineId }
            queueRoutineToDelete.value = routine
        }
    }

    private suspend fun runDeleteQueuedRoutine() {
        val routineToDelete = queueRoutineToDelete.value ?: return
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutinesState.Data> {
                reducer.deleteRoutineError(state = it, routineId = routineToDelete.id)
            }
        }
        val job = launch(exceptionHandler) {
            routinesUseCase.delete(routineToDelete)
        }
        job.join()
        queueRoutineToDelete.value = null
    }

    fun onRoutineMoved(draggedRoutineId: ID, hoveredRoutineId: ID) {
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

    fun onRoutineDuplicate(routineId: ID) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutinesState.Data> {
                reducer.duplicateRoutineError(state = it)
            }
        }
        duplicateJob = launchWhen<RoutinesState.Data>(exceptionHandler) { state ->
            duplicateRoutineUseCase(routines = state.routines, routineId = routineId)
        }
    }

    fun onRetryLoad() {
        load()
    }

    fun onSnackbarEvent(snackbarEvent: TempoSnackbarEvent) {
        launchWhen<RoutinesState.Data> { state ->
            val previousAction = state.action
            updateStateWhen(reducer::actionHandled)
            delay(SNACKBAR_ACTION_DELAY)
            when (previousAction) {
                is DeleteRoutineError -> {
                    if (snackbarEvent == ActionPerformed) {
                        onRoutineDelete(routineId = previousAction.routineId)
                    }
                }
                is DeleteRoutineSuccess -> {
                    if (snackbarEvent == ActionPerformed) {
                        queueRoutineToDelete.value = null
                    } else {
                        runDeleteQueuedRoutine()
                    }
                }
                MoveRoutineError,
                DuplicateRoutineError,
                null -> Unit
            }
        }
    }

    fun onStop() {
        updateStateWhen(reducer::actionHandled)
        launch { runDeleteQueuedRoutine() }
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
