package com.enricog.data.timer.api.settings.entities

data class TimerSettings(
    val keepScreenOnEnabled: Boolean,
    val soundEnabled: Boolean,
    val runInBackgroundEnabled: Boolean
) {

    companion object
}
