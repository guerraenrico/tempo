package com.enricog.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Segment
import com.enricog.timer.models.*
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class CountingSceneKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}

        override fun onRestartSegmentButtonClick() {}
    }

    @Test
    fun shouldShowClockAndActionBar() = composeTestRule {
        val viewState = TimerViewState.Counting(
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(
                    timeInSeconds = 1,
                    isRunning = false,
                    isCompleted = false
                ), type = SegmentStepType.STARTING
            ),
            clockBackgroundColor = Color.Blue
        )

        setContent {
            TempoTheme {
                CountingScene(viewState, timerActions)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ActionBarTestTag).assertIsDisplayed()
    }

}