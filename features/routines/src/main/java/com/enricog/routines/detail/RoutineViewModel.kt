package com.enricog.routines.detail

import androidx.hilt.lifecycle.ViewModelInject
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.routines.list.RoutinesReducer
import com.enricog.routines.list.RoutinesStateConverter
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.usecase.RoutinesUseCase

internal
class RoutineViewModel @ViewModelInject constructor(
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


    fun onStartRoutine() {
        navigationActions.goToTimer(routineId = 1)
    }

}