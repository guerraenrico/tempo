package com.enricog.features.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.models.TimerActions
import com.enricog.ui.components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class ActionsBarKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}
        override fun onRestartSegmentButtonClick() {}
        override fun onResetButtonClick() {}
        override fun onDoneButtonClick() {}
        override fun onCloseButtonClick() {}
    }

    @Test
    fun shouldShowDoneActionsWhenRoutineIsCompleted() = composeRule {
        setContent {
            TempoTheme {
                ActionsBar(
                    isTimeRunning = true,
                    isRoutineCompleted = true,
                    timerActions = timerActions
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
                    timerActions = timerActions
                )
            }
        }

        onNodeWithTag(ButtonDoneTestTag).assertDoesNotExist()
        onNodeWithTag(ButtonResetTestTag).assertDoesNotExist()
        onNodeWithTag(ButtonStartStopTestTag).assertIsDisplayed()
        onNodeWithTag(ButtonRestartTestTag).assertIsDisplayed()
    }
}
