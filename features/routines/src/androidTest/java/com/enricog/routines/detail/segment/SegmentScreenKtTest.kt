package com.enricog.routines.detail.segment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.segment.models.SegmentViewState
import com.enricog.routines.detail.segment.ui_components.SegmentFormSceneTestTag
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class SegmentScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = SegmentViewState.Idle

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentNameChange = {},
                    onSegmentTimeChange = {},
                    onSegmentTimeTypeChange = {},
                    onSegmentConfirmed = {})
            }
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderSegmentFormSceneWhenStateIsData() = composeRule {
        val viewState = SegmentViewState.Data(
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentNameChange = {},
                    onSegmentTimeChange = {},
                    onSegmentTimeTypeChange = {},
                    onSegmentConfirmed = {})
            }
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertIsDisplayed()
    }
}