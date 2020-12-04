package com.guerrae.base_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

open class BaseViewModel<ViewModelState : Any, ViewState : Any>(
    initialState: ViewModelState,
    converter: StateConverter<ViewModelState, ViewState>,
    configuration: ViewModelConfiguration = ViewModelConfiguration(debounce = 5L)
) : ViewModel() {

    private val viewModelStateFlow = MutableStateFlow(initialState)
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
            .distinctUntilChanged()
            .onEach { viewStateFlow.value = it }
//            .flowOn() TODO
            .launchIn(viewModelScope)
    }

}