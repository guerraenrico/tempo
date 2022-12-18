package com.enricog.features.timer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
import com.enricog.features.timer.ui_components.TimerCompletedSceneTestTag
import com.enricog.features.timer.ui_components.TimerCountingSceneTestTag
import com.enricog.features.timer.ui_components.TimerErrorSceneTestTag
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class TimerScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotShowCountingSceneWhenIdle() = composeRule {
        val viewState = TimerViewState.Idle

        setContent {
            TempoTheme {
                viewState.Compose(
                    onToggleTimer = {},
                    onRestartSegment = {},
                    onReset = {},
                    onDone = {},
                    onRetryLoad = {}
                )
            }
        }

        onNodeWithTag(TimerCountingSceneTestTag).assertDoesNotExist()
        onNodeWithTag(TimerErrorSceneTestTag).assertDoesNotExist()
        onNodeWithTag(TimerCompletedSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldShowCountingSceneWhenRoutineIsCounting() = composeRule {
        val viewState = TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = SegmentStep(
                count = Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = SegmentStepType.STARTING
            ),
            clockBackgroundColor = BackgroundColor(
                foreground = Color.Blue,
                ripple = null
            ),
            isSoundEnabled = true
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onToggleTimer = {},
                    onRestartSegment = {},
                    onReset = {},
                    onDone = {},
                    onRetryLoad = {}
                )
            }
        }

        onNodeWithTag(TimerCountingSceneTestTag).assertIsDisplayed()
        onNodeWithTag(TimerErrorSceneTestTag).assertDoesNotExist()
        onNodeWithTag(TimerCompletedSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderErrorSceneWhenThereIsAnError() = composeRule {
        val viewState = TimerViewState.Error(throwable = Exception())

        setContent {
            TempoTheme {
                viewState.Compose(
                    onToggleTimer = {},
                    onRestartSegment = {},
                    onReset = {},
                    onDone = {},
                    onRetryLoad = {}
                )
            }
        }

        onNodeWithTag(TimerCountingSceneTestTag).assertDoesNotExist()
        onNodeWithTag(TimerErrorSceneTestTag).assertIsDisplayed()
        onNodeWithTag(TimerCompletedSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldShowCompleteSceneWhenRoutineIsComplete() = composeRule {
        val viewState = TimerViewState.Completed

        setContent {
            TempoTheme {
                viewState.Compose(
                    onToggleTimer = {},
                    onRestartSegment = {},
                    onReset = {},
                    onDone = {},
                    onRetryLoad = {}
                )
            }
        }

        onNodeWithTag(TimerCountingSceneTestTag).assertDoesNotExist()
        onNodeWithTag(TimerErrorSceneTestTag).assertDoesNotExist()
        onNodeWithTag(TimerCompletedSceneTestTag).assertIsDisplayed()
    }
}
