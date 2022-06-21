package com.enricog.features.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class ActionsBarKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowDoneActionsWhenRoutineIsCompleted() = composeRule {
        setContent {
            TempoTheme {
                ActionsBar(
                    isTimeRunning = true,
                    isRoutineCompleted = true,
                    onStartStopButtonClick = {},
                    onRestartSegmentButtonClick = {},
                    onResetButtonClick = {},
                    onDoneButtonClick = {}
                )
            }
        }

        onNodeWithTag(ButtonDoneTestTag).assertIsDisplayed()
        onNodeWithTag(ButtonResetTestTag).assertIsDisplayed()
        onNodeWithTag(ButtonStartStopTestTag).assertDoesNotExist()
        onNodeWithTag(ButtonRestartTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldShowRunningActionsWhenRoutineIsNotCompleted() = composeRule {
        setContent {
            TempoTheme {
                ActionsBar(
                    isTimeRunning = true,
                    isRoutineCompleted = false,
                    onStartStopButtonClick = {},
                    onRestartSegmentButtonClick = {},
                    onResetButtonClick = {},
                    onDoneButtonClick = {}
                )
            }
        }

        onNodeWithTag(ButtonDoneTestTag).assertDoesNotExist()
        onNodeWithTag(ButtonResetTestTag).assertDoesNotExist()
        onNodeWithTag(ButtonStartStopTestTag).assertIsDisplayed()
        onNodeWithTag(ButtonRestartTestTag).assertIsDisplayed()
    }
}
