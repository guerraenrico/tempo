package com.enricog.base_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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

    protected inline fun <reified T : ViewModelState> launchWhen(
        noinline block: suspend CoroutineScope.(T) -> Unit
    ) {
        (state as? T)?.let {
            viewModelScope.launch {
                block(it)
            }
        }
    }
}
