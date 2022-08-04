package com.enricog.features.routines.detail.routine

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.ui.components.textField.TimeText
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

    private var saveRoutineJob by autoCancelableJob()

    init {
        val input = RoutineRoute.extractInput(savedStateHandle)
        load(input)
    }

    private fun load(input: RoutineRouteInput) {
        viewModelScope.launch {
            val routine = routineUseCase.get(routineId = input.routineId)
            updateState { reducer.setup(routine = routine) }
        }
    }

    fun onRoutineNameTextChange(textFieldValue: TextFieldValue) {
        updateStateWhen<RoutineState.Data> {
            reducer.updateRoutineName(state = it, textFieldValue = textFieldValue)
        }
    }

    fun onRoutineStartTimeOffsetChange(text: TimeText) {
        updateStateWhen<RoutineState.Data> {
            reducer.updateRoutineStartTimeOffset(state = it, text = text)
        }
    }

    fun onRoutineStartTimeInfoClick() = launch {
        navigationActions.openRoutineStartTimeInfo()
    }

    fun onRoutineBack() = launch {
        navigationActions.goBack()
    }

    fun onRoutineSave() = runWhen<RoutineState.Data> { stateData ->
        val errors = validator.validate(inputs = stateData.inputs)
        if (errors.isEmpty()) {
            save(routine = stateData.inputs.mergeToRoutine(routine = stateData.routine))
        } else {
            updateStateWhen<RoutineState.Data> {
                reducer.applyRoutineErrors(state = it, errors = errors)
            }
        }
    }

    private fun save(routine: Routine) {
        saveRoutineJob = viewModelScope.launch {
            val routineId = routineUseCase.save(routine = routine)
            when {
                routine.isNew -> navigationActions.goToRoutineSummary(routineId = routineId)
                else -> navigationActions.goBack()
            }
        }
    }
}
