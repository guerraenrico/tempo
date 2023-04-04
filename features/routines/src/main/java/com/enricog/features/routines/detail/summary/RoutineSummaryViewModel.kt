package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.entities.ID
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentSuccess
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DuplicateSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.MoveSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.usecase.DeleteSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.DuplicateSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.GetRoutineUseCase
import com.enricog.features.routines.detail.summary.usecase.GetTimerThemeUserCase
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
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

@HiltViewModel
internal class RoutineSummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: RoutineSummaryStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineSummaryReducer,
    private val getTimerThemeUserCase: GetTimerThemeUserCase,
    private val getRoutineUseCase: GetRoutineUseCase,
    private val deleteSegmentUseCase: DeleteSegmentUseCase,
    private val moveSegmentUseCase: MoveSegmentUseCase,
    private val duplicateSegmentUseCase: DuplicateSegmentUseCase,
    private val validator: RoutineSummaryValidator
) : BaseViewModel<RoutineSummaryState, RoutineSummaryViewState>(
    dispatchers = dispatchers,
    converter = converter,
    initialState = RoutineSummaryState.Idle,
) {
    private val input = RoutineSummaryRoute.extractInput(savedStateHandle)
    private var loadJob by autoCancelableJob()
    private var moveJob by autoCancelableJob()
    private var duplicateJob by autoCancelableJob()

    private val queueSegmentToDelete = MutableStateFlow<Segment?>(null)

    init {
        load(input)
    }

    private fun load(input: RoutineSummaryRouteInput) {
        val timerThemeFlow = getTimerThemeUserCase()
        val routineFlow = getRoutineUseCase(input.routineId)
            .combine(queueSegmentToDelete) { routine, segmentToDelete -> routine.copy(segments = routine.segments.filter { it != segmentToDelete }) }

        loadJob = combine(routineFlow, timerThemeFlow) { routine, timerTheme -> routine to timerTheme }
            .onEach { (routine, timerTheme) ->
                updateState {
                    reducer.setup(state = it, routine = routine, timerTheme = timerTheme)
                }
            }
            .catch { throwable ->
                TempoLogger.e(throwable = throwable, message = "Error loading routine summary")
                updateState { reducer.error(throwable = throwable) }
            }
            .launchIn(viewModelScope)
    }

    fun onRetryLoad() {
        load(input)
    }

    fun onSegmentAdd() {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = ID.new())
        }
    }

    fun onSegmentSelected(segmentId: ID) {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = segmentId)
        }
    }

    fun onSegmentDelete(segmentId: ID) {
        setSegmentToDeleteInQueue(segmentId = segmentId)
        updateStateWhen<RoutineSummaryState.Data> { state ->
            reducer.deleteSegmentSuccess(state = state)
        }
    }

    private fun setSegmentToDeleteInQueue(segmentId: ID) {
        launch {
            runDeleteQueuedSegment()
            runWhen<RoutineSummaryState.Data> { state ->
                val segment = state.routine.segments.first { it.id == segmentId }
                queueSegmentToDelete.value = segment
            }
        }
    }

    fun onSegmentDuplicate(segmentId: ID) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutineSummaryState.Data> {
                reducer.duplicateSegmentError(state = it)
            }
        }
        duplicateJob = launch(exceptionHandler) {
            runDeleteQueuedSegment()
            runWhen<RoutineSummaryState.Data> { state ->
                duplicateSegmentUseCase(routine = state.routine, segmentId = segmentId)
            }
        }
    }

    fun onSegmentMoved(draggedSegmentId: ID, hoveredSegmentId: ID) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutineSummaryState.Data> { reducer.moveSegmentError(state = it) }
        }
        moveJob = launch(exceptionHandler) {
            runDeleteQueuedSegment()
            runWhen<RoutineSummaryState.Data> {
                moveSegmentUseCase(
                    routine = it.routine,
                    draggedSegmentId = draggedSegmentId,
                    hoveredSegmentId = hoveredSegmentId
                )
            }
        }
    }

    fun onRoutineStart() {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            val errors = validator.validate(routine = stateData.routine)
            if (errors.isEmpty()) {
                navigationActions.goToTimer(routineId = stateData.routine.id)
            } else {
                updateStateWhen<RoutineSummaryState.Data> {
                    reducer.applyRoutineErrors(state = it, errors = errors)
                }
            }
        }
    }

    fun onRoutineEdit() {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            navigationActions.goToRoutine(routineId = stateData.routine.id)
        }
    }

    fun onBack() {
        launch {
            navigationActions.goBack()
        }
    }

    fun onSnackbarEvent(snackbarEvent: TempoSnackbarEvent) {
        launchWhen<RoutineSummaryState.Data> {
            val previousAction = it.action
            updateStateWhen(reducer::actionHandled)
            delay(SNACKBAR_ACTION_DELAY)
            when (previousAction) {
                is DeleteSegmentError -> {
                    if (snackbarEvent == ActionPerformed) {
                        onSegmentDelete(segmentId = previousAction.segmentId)
                    }
                }
                is DeleteSegmentSuccess -> {
                    if (snackbarEvent == ActionPerformed) {
                        queueSegmentToDelete.value = null
                    } else {
                        runDeleteQueuedSegment()
                    }
                }
                MoveSegmentError,
                DuplicateSegmentError,
                null -> Unit
            }
        }
    }

    fun onStop() {
        updateStateWhen(reducer::actionHandled)
        launch { runDeleteQueuedSegment() }
    }

    private suspend fun runDeleteQueuedSegment() {
        val segmentToDelete = queueSegmentToDelete.value ?: return
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutineSummaryState.Data> {
                reducer.deleteSegmentError(state = it, segmentId = segmentToDelete.id)
            }
        }
        val job = launch(exceptionHandler) {
            val updatedState = updateStateWhen<RoutineSummaryState.Data> {
                reducer.actionHandled(state = it)
            }
            if (updatedState is RoutineSummaryState.Data) {
                deleteSegmentUseCase(routine = updatedState.routine, segmentId = segmentToDelete.id)
            }
        }
        job.join()
        queueSegmentToDelete.value = null
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
