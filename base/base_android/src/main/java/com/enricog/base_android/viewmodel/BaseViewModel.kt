package com.enricog.base_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
open class BaseViewModel<ViewModelState : Any, ViewState : Any>(
    initialState: ViewModelState,
    converter: StateConverter<ViewModelState, ViewState>,
    dispatchers: CoroutineDispatchers,
    configuration: ViewModelConfiguration = ViewModelConfiguration(debounce = 50L),
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
            .onEach(::onStateUpdated)
            .map(converter::convert)
            .flowOn(dispatchers.cpu)
            .distinctUntilChanged()
            .onEach { viewStateFlow.value = it }
            .launchIn(viewModelScope)
    }

    protected open fun onStateUpdated(currentState: ViewModelState) {}

    protected inline fun <reified T : ViewModelState> runWhen(block: (T) -> Unit) {
        (state as? T)?.also(block)
    }
}