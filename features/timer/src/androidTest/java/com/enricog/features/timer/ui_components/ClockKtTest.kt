package com.enricog.features.timer.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.seconds
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class ClockKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowClockWitSeconds() = composeRule {
        val color = Color.Red
        val time = 10.seconds
        setContent {
            TempoTheme {
                Clock(backgroundColor = color, seconds = time, size = 200.dp)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTimeTextTestTag).assertTextEquals("10")
    }

    @Test
    fun shouldShowClockWitMinutesAndSeconds() = composeRule {
        val color = Color.Red
        val time = 90.seconds
        setContent {
            TempoTheme {
                Clock(backgroundColor = color, seconds = time, size = 200.dp)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTimeTextTestTag).assertTextEquals("01:30")
    }
}
