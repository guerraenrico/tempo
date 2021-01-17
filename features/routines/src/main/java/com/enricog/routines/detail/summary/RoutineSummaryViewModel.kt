package com.enricog.routines.detail.summary

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class RoutineSummaryViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutineSummaryStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineSummaryReducer,
    private val routineSummaryUseCase: RoutineSummaryUseCase
) : BaseViewModel<RoutineSummaryState, RoutineSummaryViewState>(
    dispatchers = dispatchers,
    converter = converter,
    initialState = RoutineSummaryState.Idle,
) {

    fun load(routineId: Long) {
        routineSummaryUseCase.get(routineId)
            .onEach { routine -> state = reducer.setup(routine) }
            .launchIn(viewModelScope)
    }

    fun onSegmentAdd() = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = null)
    }

    fun onSegmentSelected(segment: Segment) = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToSegment(routineId = stateData.routine.id, segmentId = segment.id)
    }

    fun onSegmentDelete(segment: Segment) = launchWhen<RoutineSummaryState.Data> { stateData ->
        val newState = reducer.deleteSegment(stateData, segment)
        state = newState
        routineSummaryUseCase.update(newState.routine)
    }

    fun onRoutineStart() = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToTimer(routineId = stateData.routine.id)
    }

    fun onRoutineEdit() = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToRoutine(routineId = stateData.routine.id)
    }

    fun onBack() {
        navigationActions.routinesBack()
    }
}