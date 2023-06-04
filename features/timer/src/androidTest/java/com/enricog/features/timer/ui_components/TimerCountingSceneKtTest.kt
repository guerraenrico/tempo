package com.enricog.features.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.Background
import com.enricog.ui.components.button.icon.TempoIconButtonSize
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
            timeInSeconds = 1,
            clockBackground = Background(
                background = Color.Blue,
                ripple = null
            ),
            clockOnBackgroundColor = Color.White,
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
                TimerCountingScene(
                    state = viewState,
                    onPlay = {},
                    onBack = {},
                    onNext = {}
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
            timeInSeconds = 1,
            clockBackground = Background(
                background = Color.Blue,
                ripple = null
            ),
            clockOnBackgroundColor = Color.White,
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
                TimerCountingScene(
                    state = viewState,
                    onPlay = {},
                    onBack = {},
                    onNext = {}
                )
            }
        }

        onNodeWithTag(StepTitleTestTag).assertTextEquals("GO")
        onNodeWithTag(SegmentNameTestTag).assertTextEquals("segment name")
    }
}
