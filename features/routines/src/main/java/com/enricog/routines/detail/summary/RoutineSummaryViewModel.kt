package com.enricog.routines.detail.summary

import androidx.hilt.lifecycle.ViewModelInject
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.routine.models.RoutineState
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.navigation.RoutinesNavigationActions

internal class RoutineSummaryViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutineSummaryStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineSummaryReducer
) : BaseViewModel<RoutineSummaryState, RoutineSummaryViewState>(
    dispatchers = dispatchers,
    converter = converter,
    initialState = RoutineSummaryState.Idle,
) {

    fun onAddSegmentClick() = runWhen<RoutineSummaryState.Data> { stateData ->
        state = reducer.editNewSegment(stateData)
    }

    fun onSegmentClick(segment: Segment) = runWhen<RoutineSummaryState.Data> { stateData ->
        state = reducer.editSegment(stateData, segment)
    }

    fun onSegmentDelete(segment: Segment) = runWhen<RoutineSummaryState.Data> { stateData ->
        state = reducer.deleteSegment(stateData, segment)
    }

    fun onStartRoutine() = runWhen<RoutineSummaryState.Data> { stateData ->
        navigationActions.goToTimer(routineId = stateData.routine.id)
    }

}