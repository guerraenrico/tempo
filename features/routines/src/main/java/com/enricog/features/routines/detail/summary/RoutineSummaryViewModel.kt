package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Segment
import com.enricog.entities.ID
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.MoveSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
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
    private val routineSummaryUseCase: RoutineSummaryUseCase,
    private val moveSegmentUseCase: MoveSegmentUseCase,
    private val validator: RoutineSummaryValidator
) : BaseViewModel<RoutineSummaryState, RoutineSummaryViewState>(
    dispatchers = dispatchers,
    converter = converter,
    initialState = RoutineSummaryState.Idle,
) {
    private val input = RoutineSummaryRoute.extractInput(savedStateHandle)
    private var loadJob by autoCancelableJob()
    private var deleteJob by autoCancelableJob()
    private var moveJob by autoCancelableJob()

    init {
        load(input)
    }

    private fun load(input: RoutineSummaryRouteInput) {
        loadJob = routineSummaryUseCase.get(input.routineId)
            .onEach { routine ->
                updateState { reducer.setup(routine = routine) }
            }
            .catch { throwable ->
                TempoLogger.e(throwable = throwable, message = "Error loading routine summary")
                updateState { reducer.error(throwable = throwable) }
            }
            .launchIn(viewModelScope)
    }

    fun onRetryLoadClick() {
        load(input)
    }

    fun onSegmentAdd() {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = ID.new())
        }
    }

    fun onSegmentSelected(segment: Segment) {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = segment.id)
        }
    }

    fun onSegmentDelete(segment: Segment) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutineSummaryState.Data> {
                reducer.deleteSegmentError(state = it, segment = segment)
            }
        }
        deleteJob = launchWhen<RoutineSummaryState.Data>(exceptionHandler) {
            routineSummaryUseCase.deleteSegment(routine = it.routine, segment = segment)
        }
    }

    fun onSegmentMoved(segment: Segment, hoveredSegment: Segment?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable)
            updateStateWhen<RoutineSummaryState.Data> { reducer.segmentMoveError(state = it) }
        }
        moveJob = launchWhen<RoutineSummaryState.Data>(exceptionHandler) {
            moveSegmentUseCase(it.routine, segment, hoveredSegment)
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
        runWhen<RoutineSummaryState.Data> {
            if (snackbarEvent == TempoSnackbarEvent.ActionPerformed) {
                when (it.action) {
                    is DeleteSegmentError -> onSegmentDelete(it.action.segment)
                    MoveSegmentError,
                    null -> { /* no-op */
                    }
                }
            }
        }
        updateStateWhen<RoutineSummaryState.Data> { reducer.onActionHandled(it) }
    }
}
