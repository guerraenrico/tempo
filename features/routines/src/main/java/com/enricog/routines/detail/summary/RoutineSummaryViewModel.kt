package com.enricog.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.navigation.RoutinesNavigationConstants.RoutineSummary.routeIdParamName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RoutineSummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: RoutineSummaryStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineSummaryReducer,
    private val routineSummaryUseCase: RoutineSummaryUseCase,
    private val validator: RoutineSummaryValidator
) : BaseViewModel<RoutineSummaryState, RoutineSummaryViewState>(
    dispatchers = dispatchers,
    converter = converter,
    initialState = RoutineSummaryState.Idle,
) {

    init {
        val routineId = savedStateHandle.get<Long>(routeIdParamName)!!
        load(routineId)
    }

    private fun load(routineId: Long) {
        routineSummaryUseCase.get(routineId)
            .onEach { routine ->
                updateState { reducer.setup(routine = routine) }
            }
            .launchIn(viewModelScope)
    }

    fun onSegmentAdd() = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = null)
    }

    fun onSegmentSelected(segment: Segment) = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = segment.id)
    }

    fun onSegmentDelete(segment: Segment) {
        viewModelScope.launch {
            val newState = updateStateWhen<RoutineSummaryState.Data> {
                reducer.deleteSegment(state = it, segment = segment)
            }
            if (newState is RoutineSummaryState.Data) {
                routineSummaryUseCase.update(routine = newState.routine)
            }
        }
    }

    fun onRoutineStart() = runWhen<RoutineSummaryState.Data> { stateData ->
        val errors = validator.validate(routine = stateData.routine)
        if (errors.isEmpty()) {
            navigationActions.goToTimer(routineId = stateData.routine.id)
        } else {
            updateStateWhen<RoutineSummaryState.Data> {
                reducer.applyRoutineErrors(state = it, errors = errors)
            }
        }
    }

    fun onRoutineEdit() = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToRoutine(routineId = stateData.routine.id)
    }

    fun onBack() {
        navigationActions.routinesBack()
    }
}
