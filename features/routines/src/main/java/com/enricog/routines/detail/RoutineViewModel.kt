package com.enricog.routines.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.base_android.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.RoutineState
import com.enricog.routines.detail.models.RoutineViewState
import com.enricog.routines.detail.validation.RoutineValidator
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.usecase.RoutineUseCase
import kotlinx.coroutines.launch

internal class RoutineViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutineStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineReducer,
    private val validator: RoutineValidator,
    private val routineUseCase: RoutineUseCase
) : BaseViewModel<RoutineState, RoutineViewState>(
    initialState = RoutineState.Idle,
    converter = converter,
    dispatchers = dispatchers,
    configuration = ViewModelConfiguration(debounce = 0)
) {

    fun load(routineId: Long) {
        viewModelScope.launch {
            val routine = routineUseCase.get(routineId)
            state = reducer.setup(routine)
        }
    }

    fun onRoutineNameTextChange(text: String) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.updateRoutineName(stateData, text)
    }

    fun onRoutineStartTimeOffsetChange(seconds: Long) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.updateRoutineStartTimeOffset(stateData, seconds)
    }

    fun onAddSegmentClick() = runWhen<RoutineState.Data> { stateData ->
        state = reducer.editNewSegment(stateData)
    }

    fun onSegmentClick(segment: Segment) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.editSegment(stateData, segment)
    }

    fun onSegmentNameTextChange(text: String) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.updateSegmentName(stateData, text)
    }

    fun onSegmentTimeChange(seconds: Long) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.updateSegmentTime(stateData, seconds)
    }

    fun onSegmentTypeChange(timeType: TimeType) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.updateSegmentTimeType(stateData, timeType)
    }

    fun onSegmentConfirmed() {

    }

    fun onSegmentBack() = runWhen<RoutineState.Data> { stateData ->
        state = reducer.closeEditSegment(stateData)
    }

    fun onStartRoutine() {
        navigationActions.goToTimer(routineId = 1)
    }

}