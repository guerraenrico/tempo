package com.enricog.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.sortedByRank
import com.enricog.navigation.routes.RoutineSummaryRoute
import com.enricog.navigation.routes.RoutineSummaryRouteInput
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
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
        viewModelScope.launch {
            val newState = updateStateWhen<RoutineSummaryState.Data> {
                reducer.deleteSegment(state = it, segment = segment)
            }
            if (newState is RoutineSummaryState.Data) {
                routineSummaryUseCase.update(routine = newState.routine)
            }
        }
    }

    fun onSegmentMoved(segment: Segment, newIndex: Int) {
        updateStateWhen<RoutineSummaryState.Data> { stateData ->
            val itemIndex = stateData.routine.segments.indexOf(segment)

            if (itemIndex == newIndex || itemIndex < 0) {
                return@updateStateWhen stateData
            }

            val rank1 = when {
                newIndex > itemIndex -> stateData.routine.segments.getOrNull(newIndex)?.rank
                else -> stateData.routine.segments.getOrNull(newIndex - 1)?.rank
            }
            val rank2 = when {
                newIndex > itemIndex -> stateData.routine.segments.getOrNull(newIndex + 1)?.rank
                else -> stateData.routine.segments.getOrNull(newIndex)?.rank
            }
            val newRank = Rank.calculate(rank1 = rank1, rank2 = rank2)
            val segments = stateData.routine.segments
                .map {
                    if (it.id == segment.id) {
                        it.copy(rank = newRank)
                    } else {
                        it
                    }
                }
                .sortedByRank()

            val routine = stateData.routine.copy(segments = segments)
            stateData.copy(routine = routine)
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
