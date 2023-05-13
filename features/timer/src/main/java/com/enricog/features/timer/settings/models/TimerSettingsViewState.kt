package com.enricog.features.timer.settings.models

import androidx.annotation.StringRes

internal sealed class TimerSettingsViewState {

    object Idle : TimerSettingsViewState()

    data class Data(
        val soundSetting: Setting,
        val runInBackgroundSetting: Setting
    ): TimerSettingsViewState() {

        data class Setting(
            @StringRes val title: Int,
            val enabled: Boolean
        )
    }
}