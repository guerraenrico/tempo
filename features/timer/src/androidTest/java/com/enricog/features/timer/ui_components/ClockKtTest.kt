package com.enricog.features.timer.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class ClockKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowClockWitSeconds() = composeRule {
        setContent {
            TempoTheme {
                Clock(timeInSeconds = 10)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTimeTextTestTag).assertTextEquals("10")
    }

    @Test
    fun shouldShowClockWitMinutesAndSeconds() = composeRule {
        setContent {
            TempoTheme {
                Clock(timeInSeconds = 90)
            }
        }

        onNodeWithTag(ClockTestTag).assertIsDisplayed()
        onNodeWithTag(ClockTimeTextTestTag).assertTextEquals("01:30")
    }
}
