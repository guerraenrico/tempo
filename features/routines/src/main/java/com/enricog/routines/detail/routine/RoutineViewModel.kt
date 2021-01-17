package com.enricog.routines.detail.routine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.base_android.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.entities.routines.Routine
import com.enricog.routines.detail.routine.models.RoutineState
import com.enricog.routines.detail.routine.models.RoutineViewState
import com.enricog.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
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

    fun onRoutineBack() {
        navigationActions.routinesBack()
    }

    fun onRoutineSave() = runWhen<RoutineState.Data> { stateData ->
        val errors = validator.validate(stateData.routine)
        if (errors.isEmpty()) {
            save(stateData.routine)
        } else {
            state = reducer.applyRoutineErrors(stateData, errors)
        }
    }

    private fun save(routine: Routine) {
        startRoutineJob = viewModelScope.launch {
            val routineId = routineUseCase.save(routine)

            if (routine.isNew) {
                navigationActions.goToRoutineSummary(routineId = routineId)
            } else {
                navigationActions.routinesBack()
            }
        }
    }
}