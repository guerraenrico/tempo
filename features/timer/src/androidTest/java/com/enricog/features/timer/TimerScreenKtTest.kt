package com.enricog.features.timer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.Background
import com.enricog.features.timer.ui_components.TimerCompletedSceneTestTag
import com.enricog.features.timer.ui_components.TimerCountingSceneTestTag
import com.enricog.features.timer.ui_components.TimerErrorSceneTestTag
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.textField.timeText
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
                    onPlay = {},
                    onBack = {},
                    onNext = {},
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
            timeInSeconds = 1,
            clockBackground = Background(
                background = Color.Blue,
                ripple = null
            ),
            clockOnBackgroundColor = Color.White,
            isSoundEnabled = true,
            timerActions = TimerViewState.Counting.Actions(
                back = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_back,
                    contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                play = TimerViewState.Counting.Actions.Button(
                    iconResId =  R.drawable.ic_timer_stop,
                    contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                next = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_next,
                    contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            )
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onPlay = {},
                    onBack = {},
                    onNext = {},
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
                    onPlay = {},
                    onBack = {},
                    onNext = {},
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
        val viewState = TimerViewState.Completed(
            effectiveTotalTime = "00:00".timeText,
            skipCount = 0
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onPlay = {},
                    onBack = {},
                    onNext = {},
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
