package com.enricog.features.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.drawable.assertDrawable
import com.enricog.core.compose.testing.invoke
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class TimerCompletedSceneKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldShowElements() = composeTestRule {
        val viewState = TimerViewState.Completed(
            effectiveTotalTime = "10:30".timeText,
            skipCount = 3
        )

        setContent {
            TempoTheme {
                TimerCompletedScene(state = viewState, onReset = {}, onDone = {})
            }
        }

        onNodeWithTag(TimerCompletedSceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutineCompletedTitleTestTag).assertTextEquals("Completed")
        onNodeWithTag(RoutineEffectiveTimeTestTag).assertTextEquals("10:30")
        onNodeWithTag(RoutineSkipCountTestTag).assertTextEquals("3")
        onNodeWithTag(ButtonResetTestTag).assertDrawable(R.drawable.ic_timer_restart)
        onNodeWithTag(ButtonDoneTestTag).assertDrawable(R.drawable.ic_timer_done)
    }
}