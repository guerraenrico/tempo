package com.enricog.timer_service.controller

import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.core.coroutine.scope.ApplicationCoroutineScope
import com.enricog.entities.routines.Routine
import com.enricog.timer_service.TimerReducer
import com.enricog.timer_service.models.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TimerControllerImpl @Inject constructor(
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    private val reducer: TimerReducer
): TimerController {

    private val _state = MutableStateFlow<TimerState>(TimerState.Idle)
    override val state: Flow<TimerState> = _state

    private var startJob by autoCancelableJob()
    private var countingJob by autoCancelableJob()

    init {
        _state
            .onEach(::onStateUpdated)
            .launchIn(scope)
    }

    override fun start(routine: Routine) {
        startJob = scope.launch {
            _state.update { reducer.setup(routine) }

            delay(1000)

            _state.update { reducer.toggleTimeRunning(it) }
        }
    }

    override fun stop() {
        stopCounting()
    }

    override fun toggleTimeRunning() {
        _state.update { reducer.toggleTimeRunning(it) }
    }

    override fun restart() {
        _state.update { reducer.restartTime(it) }
    }

    private fun onStateUpdated(currentState: TimerState) {
        if (currentState is TimerState.Counting) {
            if (currentState.isCountRunning) {
                startCounting()
                return
            }
            if (currentState.isCountCompleted) {
                stopCounting()
                onCountCompleted()
                return
            }
        }
        stopCounting()
    }

    private fun onCountCompleted() {
        scope.launch {
            delay(1000)
            _state.update { reducer.nextStep(it) }
        }
    }

    private fun startCounting() {
        if (countingJob?.isActive == true) return
        countingJob = scope.launch {
            while (true) {
                delay(1000)
                _state.update { reducer.progressTime(it) }
            }
        }
    }

    private fun stopCounting() {
        countingJob?.cancel()
    }
}