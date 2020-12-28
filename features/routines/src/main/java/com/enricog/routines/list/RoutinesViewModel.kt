package com.enricog.routines.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.Routine
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.usecase.RoutinesUseCase
import kotlinx.coroutines.launch

internal class RoutinesViewModel @ViewModelInject constructor(
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
        viewModelScope.launch {
            val routines = routinesUseCase.getAll()
            state = reducer.setup(routines)
        }
    }

    fun onCreateRoutineClick() {
        navigationActions.goToRoutine(routineId = null)
    }

    fun onRoutineClick(routine: Routine) {
        navigationActions.goToRoutine(routineId = routine.id)
    }
}