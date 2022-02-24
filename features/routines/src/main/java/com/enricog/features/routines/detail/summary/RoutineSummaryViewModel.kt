package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.entities.ID
import com.enricog.entities.routines.Segment
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        val input = RoutineSummaryRoute.extractInput(savedStateHandle)
        load(input)
    }

    private fun load(input: RoutineSummaryRouteInput) {
        routineSummaryUseCase.get(input.routineId)
            .onEach { routine ->
                updateState { reducer.setup(routine = routine) }
            }
            .launchIn(viewModelScope)
    }

    fun onSegmentAdd() = launchWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = ID.new())
    }

    fun onSegmentSelected(segment: Segment) = launchWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = segment.id)
    }

    fun onSegmentDelete(segment: Segment) {
        launch {
            val newState = updateStateWhen<RoutineSummaryState.Data> {
                reducer.deleteSegment(state = it, segment = segment)
            }
            if (newState is RoutineSummaryState.Data) {
                routineSummaryUseCase.update(routine = newState.routine)
            }
        }
    }

    fun onSegmentMoved(segment: Segment, hoveredSegment: Segment?) {
        launchWhen<RoutineSummaryState.Data> { stateData ->
            moveSegmentUseCase(stateData.routine, segment, hoveredSegment)
        }
    }

    fun onRoutineStart() = launchWhen<RoutineSummaryState.Data> { stateData ->
        val errors = validator.validate(routine = stateData.routine)
        if (errors.isEmpty()) {
            navigationActions.goToTimer(routineId = stateData.routine.id)
        } else {
            updateStateWhen<RoutineSummaryState.Data> {
                reducer.applyRoutineErrors(state = it, errors = errors)
            }
        }
    }

    fun onRoutineEdit() = launchWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToRoutine(routineId = stateData.routine.id)
    }

    fun onBack() = launch {
        navigationActions.goBack()
    }
}