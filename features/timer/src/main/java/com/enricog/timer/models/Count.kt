package com.enricog.timer.models

import com.enricog.entities.Seconds

internal data class Count(
    val seconds: Seconds,
    val isRunning: Boolean,
    val isCompleted: Boolean
) {
    companion object {
        fun idle(seconds: Seconds): Count {
            return Count(seconds = seconds, isRunning = false, isCompleted = false)
        }

        fun start(seconds: Seconds): Count {
            return Count(seconds = seconds, isRunning = true, isCompleted = false)
        }
    }
}
