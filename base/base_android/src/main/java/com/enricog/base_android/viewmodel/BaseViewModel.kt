package com.enricog.base_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enricog.core.coroutine.CoroutineDispatchers
import kotlinx.coroutines.flow.*

open class BaseViewModel<ViewModelState : Any, ViewState : Any>(
    initialState: ViewModelState,
    converter: StateConverter<ViewModelState, ViewState>,
    dispatchers: CoroutineDispatchers,
    configuration: ViewModelConfiguration = ViewModelConfiguration(debounce = 5L),
) : ViewModel() {

    protected val viewModelStateFlow = MutableStateFlow(initialState)
    protected var state: ViewModelState
        get() = viewModelStateFlow.value
        set(value) {
            viewModelStateFlow.value = value
        }

    private val viewStateFlow = MutableStateFlow<ViewState?>(null)
    val viewState: Flow<ViewState>
        get() = viewStateFlow.filterNotNull()

    init {
        viewModelStateFlow
            .debounce(configuration.debounce)
            .map { converter.convert(it) }
            .flowOn(dispatchers.cpu)
            .distinctUntilChanged()
            .onEach { viewStateFlow.value = it }
            .launchIn(viewModelScope)
    }

}