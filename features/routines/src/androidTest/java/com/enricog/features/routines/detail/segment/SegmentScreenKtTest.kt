package com.enricog.features.routines.detail.segment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.ui_components.SegmentFormSceneTestTag
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme
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
                    onSegmentConfirmed = {}
                )
            }
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderSegmentFormSceneWhenStateIsData() = composeRule {
        val viewState = SegmentViewState.Data(
            segment = SegmentFields(name = "", time = "".timeText, type = TimeType.TIMER),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentNameChange = {},
                    onSegmentTimeChange = {},
                    onSegmentTimeTypeChange = {},
                    onSegmentConfirmed = {}
                )
            }
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertIsDisplayed()
    }
}
