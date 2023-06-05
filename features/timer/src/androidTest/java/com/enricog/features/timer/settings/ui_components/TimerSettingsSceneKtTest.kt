package com.enricog.features.timer.settings.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.check.assertChecked
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.R
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

internal class TimerSettingsSceneKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldShowSettingsWithKeepScreenOnEnabled() = composeTestRule {
        val viewState = TimerSettingsViewState.Data(
            keepScreenOnSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_keep_screen_on,
                enabled = true
            ),
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = false
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = false
            )
        )

        setContent {
            TempoTheme {
                TimerSettingsScene(
                    state = viewState,
                    onKeepScreenOnClick = {},
                    onSoundClick = {},
                    onRunInBackgroundClick = {}
                )
            }
        }

        onNodeWithTag(TimerKeepScreenOnSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = true)
        }
        onNodeWithTag(TimerSoundSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = false)
        }
        onNodeWithTag(TimerBackgroundSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = false)
        }
    }

    @Test
    fun shouldShowSettingsWithSoundEnabled() = composeTestRule {
        val viewState = TimerSettingsViewState.Data(
            keepScreenOnSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_keep_screen_on,
                enabled = false
            ),
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = true
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = false
            )
        )

        setContent {
            TempoTheme {
                TimerSettingsScene(
                    state = viewState,
                    onKeepScreenOnClick = {},
                    onSoundClick = {},
                    onRunInBackgroundClick = {}
                )
            }
        }

        onNodeWithTag(TimerKeepScreenOnSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = false)
        }
        onNodeWithTag(TimerSoundSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = true)
        }
        onNodeWithTag(TimerBackgroundSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = false)
        }
    }

    @Test
    fun shouldShowSettingsWithRunInBackgroundEnabled() = composeTestRule {
        val viewState = TimerSettingsViewState.Data(
            keepScreenOnSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_keep_screen_on,
                enabled = false
            ),
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = false
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = true
            )
        )

        setContent {
            TempoTheme {
                TimerSettingsScene(
                    state = viewState,
                    onKeepScreenOnClick = {},
                    onSoundClick = {},
                    onRunInBackgroundClick = {}
                )
            }
        }

        onNodeWithTag(TimerKeepScreenOnSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = false)
        }
        onNodeWithTag(TimerSoundSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = false)
        }
        onNodeWithTag(TimerBackgroundSettingButtonTestTag).apply {
            assertIsDisplayed()
            assertChecked(checked = true)
        }
    }
}
