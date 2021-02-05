package com.enricog.routines.list

import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.Routine
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.list.usecase.RoutinesUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
internal class RoutinesViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutinesStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutinesReducer,
    private val routinesUseCase: RoutinesUseCase
) : BaseViewModel<RoutinesState, RoutinesViewState>(
    initialState = RoutinesState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    init {
        load()
    }

    private fun load() {
        routinesUseCase.getAll()
            .onEach { routines -> state = reducer.setup(routines) }
            .launchIn(viewModelScope)
    }

    fun onCreateRoutineClick() {
        navigationActions.goToRoutine(routineId = null)
    }

    fun onRoutineClick(routine: Routine) {
        navigationActions.goToRoutineSummary(routineId = routine.id)
    }

    fun onRoutineDelete(routine: Routine) = launchWhen<RoutinesState.Data> {
        routinesUseCase.delete(routine)
    }
}
