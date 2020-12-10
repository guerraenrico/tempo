package com.enricog.timer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.ClockTestTag
import org.junit.Rule
import org.junit.Test

class TimerKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}

        override fun onRestartButtonClick() {}
    }

    @Test
    fun shouldNotShowClock() = composeRule {
        val viewState = TimerViewState.Idle

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(ClockTestTag).assertDoesNotExist()
    }

    @Test
    fun shoulShowClock() = composeRule {
        val viewState = TimerViewState.Counting(timeInSeconds = 1, isRunning = true)

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
    }
}