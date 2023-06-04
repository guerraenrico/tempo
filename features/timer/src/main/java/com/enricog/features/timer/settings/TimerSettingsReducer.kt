package com.enricog.features.timer.settings

import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.features.timer.settings.models.TimerSettingsState
import javax.inject.Inject

internal class TimerSettingsReducer @Inject constructor() {

    fun updateTimerSettings(state: TimerSettingsState, timerSettings: TimerSettings): TimerSettingsState {
        if (state !is TimerSettingsState.Data) {
            return TimerSettingsState.Data(timerSettings = timerSettings)
        }

        return state.copy(timerSettings = timerSettings)
    }
}