package com.enricog.timer.models

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val timeInSeconds: Int,
        val isRunning: Boolean,
        val isCompleted: Boolean
    ) : TimerState()
}