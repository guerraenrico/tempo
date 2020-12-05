package com.guerrae.timer.models

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val timeInSeconds: Int
    ) : TimerViewState()
}