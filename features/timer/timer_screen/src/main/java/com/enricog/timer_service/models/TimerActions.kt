package com.enricog.timer_service.models

internal interface TimerActions {
    fun onStartStopButtonClick()
    fun onRestartSegmentButtonClick()
    fun onResetButtonClick()
    fun onDoneButtonClick()
    fun onCloseButtonClick()
    fun onAppInBackground()
}
