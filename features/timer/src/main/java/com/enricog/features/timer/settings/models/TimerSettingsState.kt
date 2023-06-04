package com.enricog.features.timer.settings.models

import com.enricog.data.timer.api.settings.entities.TimerSettings

internal sealed class TimerSettingsState {

    object Idle : TimerSettingsState()

    data class Data(val timerSettings: TimerSettings): TimerSettingsState()
}