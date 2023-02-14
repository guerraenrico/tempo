package com.enricog.features.routines.detail.preparation_time

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class PreparationTimeInfoSceneKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun validateElements() = composeRule {
        setContent {
            TempoTheme {
                PreparationTimeInfoScene()
            }
        }

        onNodeWithTag(PreparationTimeInfoTitleTestTag).assertIsDisplayed()
        onNodeWithTag(PreparationTimeInfoDescriptionTestTag).assertIsDisplayed()
    }
}