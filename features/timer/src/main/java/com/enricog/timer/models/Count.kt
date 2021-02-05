package com.enricog.timer.models

internal data class Count(
    val timeInSeconds: Long,
    val isRunning: Boolean,
    val isCompleted: Boolean
) {
    companion object {
        fun idle(timeInSeconds: Long): Count {
            return Count(timeInSeconds = timeInSeconds, isRunning = false, isCompleted = false)
        }

        fun start(timeInSeconds: Long): Count {
            return Count(timeInSeconds = timeInSeconds, isRunning = true, isCompleted = false)
        }
    }
}
