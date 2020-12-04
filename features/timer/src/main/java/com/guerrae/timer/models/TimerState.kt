package com.guerrae.timer.models

internal sealed class TimerState {
    object Idle: TimerState()
}