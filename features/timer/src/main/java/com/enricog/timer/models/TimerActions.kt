package com.enricog.timer.models

interface TimerActions {
    fun onStartStopButtonClick()
    fun onRestartSegmentButtonClick()
    fun onResetButtonClick()
    fun onDoneButtonClick()
}