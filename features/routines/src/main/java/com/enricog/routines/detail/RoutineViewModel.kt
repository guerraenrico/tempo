package com.enricog.routines.detail

import androidx.hilt.lifecycle.ViewModelInject
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.routines.detail.models.RoutineState
import com.enricog.routines.detail.models.RoutineViewState
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.usecase.RoutineUseCase

internal class RoutineViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutineStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutineReducer,
    private val routineUseCase: RoutineUseCase
) : BaseViewModel<RoutineState, RoutineViewState>(
    initialState = RoutineState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    fun load(routineId: Long) {

    }


    fun onStartRoutine() {
        navigationActions.goToTimer(routineId = 1)
    }

}