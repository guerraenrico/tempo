package com.enricog.routines.list

import androidx.hilt.lifecycle.ViewModelInject
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.usecase.RoutinesUseCase

internal class RoutinesViewModel @ViewModelInject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutinesStateConverter,
    private val routinesUseCase: RoutinesUseCase
) : BaseViewModel<RoutinesState, RoutinesViewState>(
    initialState = RoutinesState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {




}