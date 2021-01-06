package com.enricog.routines.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.base_android.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.EditingSegment
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

    private var startRoutineJob by autoCancelableJob()

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

    fun onSegmentDelete(segment: Segment) = runWhen<RoutineState.Data> { stateData ->
        state = reducer.deleteSegment(stateData, segment)
    }

    fun onRoutineBack() {
        navigationActions.goBackToRoutines()
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

    fun onSegmentConfirmed() = runWhen<RoutineState.Data> { stateData ->
        if (stateData.editingSegment !is EditingSegment.Data) return@runWhen

        val errors = validator.validate(stateData.editingSegment.segment)
        state = if (errors.isEmpty()) {
            reducer.updateRoutineSegment(stateData)
        } else {
            reducer.applySegmentErrors(stateData, errors)
        }
    }

    fun onSegmentBack() = runWhen<RoutineState.Data> { stateData ->
        state = reducer.closeEditSegment(stateData)
    }

    fun onStartRoutine() = runWhen<RoutineState.Data> { stateData ->
        val errors = validator.validate(stateData.routine)
        if (errors.isEmpty()) {
            saveAndStartRoutine(stateData.routine)
        } else {
            state = reducer.applyRoutineErrors(stateData, errors)
        }
    }

    private fun saveAndStartRoutine(routine: Routine) {
        startRoutineJob = viewModelScope.launch {
            val routineId = routineUseCase.save(routine)
            navigationActions.goToTimer(routineId = routineId)
        }
    }

}