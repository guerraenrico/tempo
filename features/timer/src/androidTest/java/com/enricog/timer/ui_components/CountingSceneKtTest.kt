package com.enricog.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState
import org.junit.Rule
import org.junit.Test

class CountingSceneKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}

        override fun onRestartButtonClick() {}
    }

    @Test
    fun shouldShowClockAndActionBar() = composeTestRule {
        val state = TimerViewState.Counting(timeInSeconds = 10, isRunning = false)

        setContent { CountingScene(state, timerActions) }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ActionBarTestTag).assertIsDisplayed()
    }

}