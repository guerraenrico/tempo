package com.enricog.features.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.seconds
import com.enricog.features.timer.R
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class TimerCountingSceneKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldShowTitlesClockAndActionBar() = composeTestRule {
        val viewState = TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = SegmentStep(
                count = Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = SegmentStepType.IN_PROGRESS
            ),
            clockBackgroundColor = BackgroundColor(
                foreground = Color.Blue,
                ripple = null
            ),
            isSoundEnabled = true
        )

        setContent {
            TempoTheme {
                TimerCountingScene(
                    state = viewState,
                    onToggleTimer = {},
                    onRestartSegment = {}
                )
            }
        }

        onNodeWithTag(StepTitleTestTag).assertIsDisplayed()
        onNodeWithTag(SegmentNameTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ActionBarTestTag).assertIsDisplayed()
    }

    @Test
    fun testStepTitleAndSegmentName() = composeTestRule {
        val viewState = TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = SegmentStep(
                count = Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = SegmentStepType.IN_PROGRESS
            ),
            clockBackgroundColor = BackgroundColor(
                foreground = Color.Blue,
                ripple = null
            ),
            isSoundEnabled = true
        )

        setContent {
            TempoTheme {
                TimerCountingScene(
                    state = viewState,
                    onToggleTimer = {},
                    onRestartSegment = {}
                )
            }
        }

        onNodeWithTag(StepTitleTestTag).assertTextEquals("GO")
        onNodeWithTag(SegmentNameTestTag).assertTextEquals("segment name")
    }
}
