package com.enricog.features.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.drawable.assertDrawable
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class TimerToolbarKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowToolbarWithSoundEnableIconWhenSoundIsEnabled() = composeRule {
        setContent {
            TempoTheme {
                TimerToolbar(
                    state = TimerViewState.Counting(
                        stepTitleId = R.string.title_segment_step_type_in_progress,
                        segmentName = "segment name",
                        timeInSeconds = 1,
                        isRunning = true,
                        clockBackgroundColor = TimerViewState.Counting.BackgroundColor(
                            foreground = Color.Blue,
                            ripple = null
                        ),
                        isSoundEnabled = true
                    ),
                    onCloseClick = { },
                    onSoundClick = { })
            }
        }

        onNodeWithTag(TimerToolbarCloseButtonTestTag).assertIsDisplayed()
        onNodeWithTag(TimerToolbarSoundButtonTestTag).run {
            assertIsDisplayed()
            assertDrawable(R.drawable.ic_timer_sound_enabled)
        }
    }

    @Test
    fun shouldShowToolbarWithSoundDisableIconWhenSoundIsDisabled() = composeRule {
        setContent {
            TempoTheme {
                TimerToolbar(
                    state = TimerViewState.Counting(
                        stepTitleId = R.string.title_segment_step_type_in_progress,
                        segmentName = "segment name",
                        timeInSeconds = 1,
                        isRunning = true,
                        clockBackgroundColor = TimerViewState.Counting.BackgroundColor(
                            foreground = Color.Blue,
                            ripple = null
                        ),
                        isSoundEnabled = false
                    ),
                    onCloseClick = { },
                    onSoundClick = { })
            }
        }

        onNodeWithTag(TimerToolbarCloseButtonTestTag).assertIsDisplayed()
        onNodeWithTag(TimerToolbarSoundButtonTestTag).run {
            assertIsDisplayed()
            assertDrawable(R.drawable.ic_timer_sound_disabled)
        }
    }
}