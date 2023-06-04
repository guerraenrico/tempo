package com.enricog.features.timer.settings

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.features.timer.R
import com.enricog.features.timer.settings.models.TimerSettingsState
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class TimerSettingsStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val stateConverter = TimerSettingsStateConverter()

    @Test
    fun `should map idle state`() = coroutineRule {
        val state = TimerSettingsState.Idle
        val expected = TimerSettingsViewState.Idle

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data state`() = coroutineRule {
        val state = TimerSettingsState.Data(
            timerSettings = TimerSettings.DEFAULT
        )
        val expected = TimerSettingsViewState.Data(
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = state.timerSettings.soundEnabled
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = state.timerSettings.runInBackgroundEnabled
            )
        )

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}
