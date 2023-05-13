package com.enricog.features.timer

import com.enricog.core.coroutines.scope.ApplicationCoroutineScope
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.timer.models.TimerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import java.io.Closeable
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TimerController @Inject constructor(
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    private val reducer: TimerReducer,
    private val soundPlayer: TimerSoundPlayer
) : Closeable {

    private val timer = Timer()
    private var countingTimerTask: CountingTimerTask? = null

    private val _state = MutableStateFlow<TimerState>(TimerState.Idle)
    val state: Flow<TimerState> = _state

    init {
        _state
            .onEach(::onStateUpdated)
            .launchIn(scope)
    }

    fun start(routine: Routine, timerTheme: TimerTheme, timerSettings: TimerSettings) {
        _state.update {
            reducer.setup(
                routine = routine,
                timerTheme = timerTheme,
                timerSettings = timerSettings
            )
        }

        timer.launchAfter(ONE_SECOND) {
            val newState = _state.updateAndGet { reducer.toggleTimeRunning(it) }
            soundPlayer.playFrom(state = newState)
        }
    }

    fun onPlay() {
        _state.update { reducer.toggleTimeRunning(it) }
    }

    fun onTimerSettingsChanged(timerSettings: TimerSettings) {
        _state.update { reducer.updateTimerSettings(state = it, timerSettings = timerSettings) }
    }

    fun onBack() {
        _state.update { reducer.jumpStepBack(it) }
    }

    fun onNext() {
        _state.update { reducer.jumpStepNext(it) }
    }

    private fun onStateUpdated(currentState: TimerState) {

        if (currentState !is TimerState.Counting) {
            stopCounting()
            return
        }

        when {
            currentState.isStepCountRunning -> {
                startCounting()
            }
            currentState.isStepCountCompleted -> {
                stopCounting()
                launchNextStep()
            }
            else -> {
                stopCounting()
            }
        }
    }

    private fun launchNextStep() {
        timer.launchAfter(ONE_SECOND) {
            val newState = _state.updateAndGet { reducer.nextStep(it) }
            soundPlayer.playFrom(state = newState)
        }
    }

    private fun startCounting() {
        if (countingTimerTask?.isRunning == true) return

        countingTimerTask?.cancel()
        countingTimerTask = CountingTimerTask()

        timer.scheduleAtFixedRate(
            /* task = */ countingTimerTask,
            /* delay = */ ONE_SECOND,
            /* period = */ ONE_SECOND
        )
    }

    private fun stopCounting() {
        countingTimerTask?.cancel()
    }

    override fun close() {
        stopCounting()
        soundPlayer.close()
    }

    private fun Timer.launchAfter(delay: Long, block: () -> Unit) {
        schedule(object : TimerTask() {
            override fun run() {
                block()
            }
        }, delay)
    }

    inner class CountingTimerTask : TimerTask() {

        var isRunning: Boolean = false
            private set

        override fun run() {
            isRunning = true
            val newState = _state.updateAndGet { reducer.progressTime(it) }
            soundPlayer.playFrom(state = newState)
        }

        override fun cancel(): Boolean {
            isRunning = false
            return super.cancel()
        }
    }

    private companion object {
        const val ONE_SECOND = 1000L
    }
}