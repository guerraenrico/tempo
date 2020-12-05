package com.guerrae.timer.models

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val timeInSeconds: Int
    ) : TimerState()
}