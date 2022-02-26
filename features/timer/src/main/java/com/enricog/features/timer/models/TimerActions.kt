package com.enricog.features.timer.models

interface TimerActions {
    fun onStartStopButtonClick()
    fun onRestartSegmentButtonClick()
    fun onResetButtonClick()
    fun onDoneButtonClick()
    fun onCloseButtonClick()
}
