package com.enricog.features.timer.settings

import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.features.timer.settings.models.TimerSettingsState
import com.google.common.truth.Truth.assertThat
import org.junit.Test


internal class TimerSettingsReducerTest {

    private val reducer = TimerSettingsReducer()

    @Test
    fun `should return data state with setting when state is idle`() {
        val state = TimerSettingsState.Idle
        val timerSettings = TimerSettings.DEFAULT
        val expected = TimerSettingsState.Data(timerSettings = timerSettings)

        val actual = reducer.updateTimerSettings(state = state, timerSettings = timerSettings)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update data state with setting when state is data`() {
        val state = TimerSettingsState.Data(
            timerSettings = TimerSettings.DEFAULT.copy(soundEnabled = true)
        )
        val timerSettings = TimerSettings.DEFAULT.copy(soundEnabled = false)
        val expected = TimerSettingsState.Data(timerSettings = timerSettings)

        val actual = reducer.updateTimerSettings(state = state, timerSettings = timerSettings)

        assertThat(actual).isEqualTo(expected)
    }
}
