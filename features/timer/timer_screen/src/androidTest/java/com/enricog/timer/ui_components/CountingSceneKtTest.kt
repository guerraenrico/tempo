package com.enricog.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.entities.seconds
import com.enricog.timer.R
import com.enricog.timer_service.service.Count
import com.enricog.timer_service.service.SegmentStep
import com.enricog.timer_service.service.SegmentStepType
import com.enricog.timer_service.models.TimerActions
import com.enricog.timer_service.service.TimerViewState
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class CountingSceneKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}
        override fun onRestartSegmentButtonClick() {}
        override fun onResetButtonClick() {}
        override fun onDoneButtonClick() {}
        override fun onCloseButtonClick() {}
    }

    @Test
    fun shouldShowTitlesClockAndActionBarWhenRoutineIsNotCompleted() = composeTestRule {
        val viewState = com.enricog.timer_service.service.TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            ),
            clockBackgroundColor = Color.Blue,
            isRoutineCompleted = false
        )

        setContent {
            TempoTheme {
                CountingScene(viewState, timerActions)
            }
        }

        onNodeWithTag(StepTitleTestTag).assertIsDisplayed()
        onNodeWithTag(SegmentNameTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ActionBarTestTag).assertIsDisplayed()
    }

    @Test
    fun shouldShowClockAndActionBarWhenRoutineIsCompleted() = composeTestRule {
        val viewState = com.enricog.timer_service.service.TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            ),
            clockBackgroundColor = Color.Blue,
            isRoutineCompleted = true
        )

        setContent {
            TempoTheme {
                CountingScene(viewState, timerActions)
            }
        }

        // FIXME right now assertIsNotDisplayed doesn't consider alpha at 0 to be notDisplayed
        // onNodeWithTag(StepTitleTestTag).assertIsNotDisplayed()
        // onNodeWithTag(SegmentNameTestTag).assertIsNotDisplayed()
        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ActionBarTestTag).assertIsDisplayed()
    }

    @Test
    fun testStepTitleAndSegmentName() = composeTestRule {
        val viewState = com.enricog.timer_service.service.TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            ),
            clockBackgroundColor = Color.Blue,
            isRoutineCompleted = false
        )

        setContent {
            TempoTheme {
                CountingScene(viewState, timerActions)
            }
        }

        onNodeWithTag(StepTitleTestTag).assertTextEquals("GO")
        onNodeWithTag(SegmentNameTestTag).assertTextEquals("segment name")
    }
}
