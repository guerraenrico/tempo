package com.enricog.ui.components.layout.error

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class TempoLayoutGenericErrorKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldRenderGenericError() = composeRule {
        val testTag = "TestTag"

        setContent {
            TempoTheme {
                TempoLayoutGenericError(layoutTestTag = testTag, onButtonClick = {})
            }
        }

        onNodeWithTag(testTag).assertExists()
    }
}