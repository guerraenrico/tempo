package com.enricog.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.logger.api.TempoLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
open class BaseViewModel<ViewModelState : Any, ViewState : Any>(
    initialState: ViewModelState,
    converter: StateConverter<ViewModelState, ViewState>,
    dispatchers: CoroutineDispatchers,
    configuration: ViewModelConfiguration = ViewModelConfiguration(debounce = 50L),
) : ViewModel() {

    private val viewModelStateFlow = MutableStateFlow(initialState)
    protected val state: ViewModelState get() = viewModelStateFlow.value

    private val viewStateFlow = MutableStateFlow<ViewState?>(null)
    val viewState: Flow<ViewState> = viewStateFlow.asStateFlow().filterNotNull()

    protected val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        TempoLogger.e(throwable = throwable)
    }

    init {
        viewModelStateFlow
            .debounce(configuration.debounce)
            .onEach(::onStateUpdated)
            .map(converter::convert)
            .flowOn(dispatchers.cpu)
            .distinctUntilChanged()
            .onEach { viewStateFlow.value = it }
            .catch { TempoLogger.e(throwable = it, message = "Error viewState value") }
            .launchIn(viewModelScope)
    }

    protected open fun onStateUpdated(currentState: ViewModelState) {}

    protected fun launch(
        exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(context = exceptionHandler, block = block)

    protected inline fun <reified T : ViewModelState> launchWhen(
        exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
        crossinline block: suspend CoroutineScope.(T) -> Unit
    ): Job {
        val currentState = state
        return if (currentState is T) {
            viewModelScope.launch(context = exceptionHandler) {
                block(currentState)
            }
        } else {
            Job().apply { complete() }
        }
    }

    protected inline fun <reified T : ViewModelState> runWhen(block: (T) -> Unit) {
        (state as? T)?.let(block)
    }

    protected fun updateState(block: (ViewModelState) -> ViewModelState): ViewModelState =
        viewModelStateFlow.updateAndGet(block)

    protected inline fun <reified T : ViewModelState> updateStateWhen(crossinline block: (T) -> ViewModelState): ViewModelState =
        updateState { if (it is T) block(it) else it }
}
