package com.enricog.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.enricog.base_test.compose.invoke
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class ClockKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowClockWitSeconds() = composeRule {
        val color = Color.Red
        val timeInSeconds = 10L
        setContent {
            TempoTheme {
                Clock(backgroundColor = color, timeInSeconds = timeInSeconds, size = 200.dp)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTimeTextTestTag).assertTextEquals("10")
    }

    @Test
    fun shouldShowClockWitMinutesAndSeconds() = composeRule {
        val color = Color.Red
        val timeInSeconds = 90L
        setContent {
            TempoTheme {
                Clock(backgroundColor = color, timeInSeconds = timeInSeconds, size = 200.dp)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTimeTextTestTag).assertTextEquals("01:30")
    }
}
