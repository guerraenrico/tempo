package com.enricog.features.timer.settings

import com.enricog.base.viewmodel.StateConverter
import com.enricog.features.timer.R
import com.enricog.features.timer.settings.models.TimerSettingsState
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import javax.inject.Inject

internal class TimerSettingsStateConverter @Inject constructor(
) : StateConverter<TimerSettingsState, TimerSettingsViewState> {

    override suspend fun convert(state: TimerSettingsState): TimerSettingsViewState {
        return when (state) {
            TimerSettingsState.Idle -> TimerSettingsViewState.Idle
            is TimerSettingsState.Data -> mapData(state = state)
        }
    }

    private fun mapData(state: TimerSettingsState.Data): TimerSettingsViewState.Data {
        return TimerSettingsViewState.Data(
            keepScreenOnSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_keep_screen_on,
                enabled = state.timerSettings.keepScreenOnEnabled
            ),
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = state.timerSettings.soundEnabled
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = state.timerSettings.runInBackgroundEnabled
            )
        )
    }
}