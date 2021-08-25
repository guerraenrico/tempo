package com.enricog.routines.detail.routine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.base_android.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.entities.Seconds
import com.enricog.entities.routines.Routine
import com.enricog.navigation.routes.RoutineRoute
import com.enricog.navigation.routes.RoutineRouteInput
import com.enricog.routines.detail.routine.models.RoutineState
import com.enricog.routines.detail.routine.models.RoutineViewState
import com.enricog.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RoutineViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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

    init {
        val input = RoutineRoute.extractInput(savedStateHandle)
        load(input)
    }

    private fun load(input: RoutineRouteInput) {
        viewModelScope.launch {
            val routine = routineUseCase.get(input.routineId!!)
            updateState { reducer.setup(routine) }
        }
    }

    fun onRoutineNameTextChange(text: String) {
        updateStateWhen<RoutineState.Data> {
            reducer.updateRoutineName(it, text)
        }
    }

    fun onRoutineStartTimeOffsetChange(seconds: Seconds) {
        updateStateWhen<RoutineState.Data> {
            reducer.updateRoutineStartTimeOffset(it, seconds)
        }
    }

    fun onRoutineBack() = launch {
        navigationActions.routinesBack()
    }

    fun onRoutineSave() = runWhen<RoutineState.Data> { stateData ->
        val errors = validator.validate(stateData.routine)
        if (errors.isEmpty()) {
            save(stateData.routine)
        } else {
            updateStateWhen<RoutineState.Data> {
                reducer.applyRoutineErrors(it, errors)
            }
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
