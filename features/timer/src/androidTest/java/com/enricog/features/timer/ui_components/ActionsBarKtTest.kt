package com.enricog.features.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.drawable.assertDrawable
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class ActionsBarKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowActionBarWithRelativeButtons() = composeRule {
        setContent {
            TempoTheme {
                ActionsBar(
                    timerActions = TimerViewState.Counting.Actions(
                        restart = TimerViewState.Counting.Actions.Button(
                            iconResId = R.drawable.ic_timer_back,
                            contentDescriptionResId = R.string.content_description_button_restart_routine_segment,
                            size = TempoIconButtonSize.Normal
                        ),
                        toggleStart = TimerViewState.Counting.Actions.Button(
                            iconResId = R.drawable.ic_timer_stop,
                            contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
                            size = TempoIconButtonSize.Normal
                        )
                    ),
                    onStartStopButtonClick = {},
                    onRestartSegmentButtonClick = {}
                )
            }
        }

        onNodeWithTag(ButtonToggleStartTestTag).run {
            assertIsDisplayed()
            assertDrawable(R.drawable.ic_timer_stop)
        }
        onNodeWithTag(ButtonRestartTestTag).run {
            assertIsDisplayed()
            assertDrawable(R.drawable.ic_timer_back)
        }
    }
}
