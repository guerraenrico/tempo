package com.enricog.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

fun ComposeTestRule.test(block: ComposeTestRule.() -> Unit) {
    block()
}

class ClockTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowClockWitSeconds() = composeRule.test {
        val color = Color.Red
        val timeInSeconds = 10
        setContent {
            TempoTheme {
                Clock(backgroundColor = color, timeInSeconds = timeInSeconds)
            }
        }

        onNodeWithText("10").assertIsDisplayed()
    }

    @Test
    fun shouldShowClockWitMinutesAndSeconds() = composeRule.test {
        val color = Color.Red
        val timeInSeconds = 90
        setContent {
            TempoTheme {
                Clock(backgroundColor = color, timeInSeconds = timeInSeconds)
            }
        }

        onNodeWithText("01:30").assertIsDisplayed()
    }

}