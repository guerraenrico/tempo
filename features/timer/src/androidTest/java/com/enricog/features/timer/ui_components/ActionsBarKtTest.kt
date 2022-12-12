package com.enricog.features.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.drawable.assertDrawable
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.R
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class ActionsBarKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowActionWithStopIconWhenTimeIsRunning() = composeRule {
        setContent {
            TempoTheme {
                ActionsBar(
                    isTimeRunning = true,
                    onStartStopButtonClick = {},
                    onRestartSegmentButtonClick = {}
                )
            }
        }

        onNodeWithTag(ButtonStartStopTestTag).assertIsDisplayed()
        onNodeWithTag(ButtonStartStopTestTag).assertDrawable(R.drawable.ic_timer_stop)
        onNodeWithTag(ButtonRestartTestTag).assertIsDisplayed()
    }

    @Test
    fun shouldShowActionWithPlayIconWhenTimeIsNotRunning() = composeRule {
        setContent {
            TempoTheme {
                ActionsBar(
                    isTimeRunning = false,
                    onStartStopButtonClick = {},
                    onRestartSegmentButtonClick = {}
                )
            }
        }

        onNodeWithTag(ButtonStartStopTestTag).assertIsDisplayed()
        onNodeWithTag(ButtonStartStopTestTag).assertDrawable(R.drawable.ic_timer_play)
        onNodeWithTag(ButtonRestartTestTag).assertIsDisplayed()
    }
}
