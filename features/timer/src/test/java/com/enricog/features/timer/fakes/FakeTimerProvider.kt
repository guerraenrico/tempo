package com.enricog.features.timer.fakes

import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.features.timer.TimerController
import com.enricog.features.timer.util.TimerProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

internal class FakeTimerProvider(private val scope: CoroutineScope) : TimerProvider {

    override fun get(): Timer {
        return TimeSkippingTimer(scope = scope)
    }

    inner class TimeSkippingTimer(private val scope: CoroutineScope) : Timer() {
        private var job by autoCancelableJob()

        override fun schedule(task: TimerTask, delay: Long) {
            scope.launch {
                delay(delay)
                task.run()
            }
        }

        override fun scheduleAtFixedRate(task: TimerTask, delay: Long, period: Long) {
            job = scope.launch {
                while (true) {
                    if (task is TimerController.CountingTimerTask && !task.isRunning) {
                        break
                    }
                    delay(delay)
                    if (task is TimerController.CountingTimerTask && !task.isRunning) {
                        break
                    }
                    task.run()
                }
            }
        }
    }
}
