package com.enricog.timer_service.models

import com.enricog.entities.Seconds

data class Count(
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
