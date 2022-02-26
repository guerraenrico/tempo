package com.enricog.features.routines.detail.routine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.entities.Seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
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

    private var startRoutineJob by com.enricog.core.coroutines.job.autoCancelableJob()

    init {
        val input = RoutineRoute.extractInput(savedStateHandle)
        load(input)
    }

    private fun load(input: RoutineRouteInput) {
        viewModelScope.launch {
            val routine = routineUseCase.get(input.routineId)
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
        navigationActions.goBack()
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
            when {
                routine.isNew -> navigationActions.goToRoutineSummary(routineId = routineId)
                else -> navigationActions.goBack()
            }
        }
    }
}
