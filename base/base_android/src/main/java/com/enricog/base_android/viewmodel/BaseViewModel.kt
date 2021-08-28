package com.enricog.base_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
open class BaseViewModel<ViewModelState : Any, ViewState : Any>(
    initialState: ViewModelState,
    converter: StateConverter<ViewModelState, ViewState>,
    dispatchers: CoroutineDispatchers,
    configuration: ViewModelConfiguration = ViewModelConfiguration(debounce = 50L),
) : ViewModel() {

    private val viewModelStateFlow = MutableStateFlow(initialState)
    protected val state: ViewModelState
        get() = viewModelStateFlow.value

    private val viewStateFlow = MutableStateFlow<ViewState?>(null)
    val viewState: Flow<ViewState> = viewStateFlow.asStateFlow().filterNotNull()

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
        (state as? T)?.let(block)
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }

    protected inline fun <reified T : ViewModelState> launchWhen(
        noinline block: suspend CoroutineScope.(T) -> Unit
    ) {
        (state as? T)?.let {
            viewModelScope.launch {
                block(it)
            }
        }
    }

    protected fun updateState(block: (ViewModelState) -> ViewModelState): ViewModelState {
        return viewModelStateFlow.updateAndGet(block)
    }

    protected inline fun <reified T : ViewModelState> updateStateWhen(crossinline block: (T) -> T): ViewModelState {
        return updateState {
            if (it is T) {
                block(it)
            } else {
                it
            }
        }
    }
}
