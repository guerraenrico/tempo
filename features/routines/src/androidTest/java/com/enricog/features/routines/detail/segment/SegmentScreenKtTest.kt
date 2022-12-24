package com.enricog.features.routines.detail.segment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.testing.invoke
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.ui_components.SegmentErrorSceneTestTag
import com.enricog.features.routines.detail.segment.ui_components.SegmentFormSceneTestTag
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

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
                    onSegmentSave = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertDoesNotExist()
        onNodeWithTag(SegmentErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderSegmentFormSceneWhenStateIsData() = composeRule {
        val viewState = SegmentViewState.Data(
            segment = SegmentFields(
                name = "".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentNameChange = {},
                    onSegmentTimeChange = {},
                    onSegmentTimeTypeChange = {},
                    onSegmentSave = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertIsDisplayed()
        onNodeWithTag(SegmentErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderSegmentErrorSceneWhenStateIsError() = composeRule {
        val viewState = SegmentViewState.Error(throwable = Exception())

        setContent {
            viewState.Compose(
                onSegmentNameChange = {},
                onSegmentTimeChange = {},
                onSegmentTimeTypeChange = {},
                onSegmentSave = {},
                onRetryLoad = {},
                onSnackbarEvent = {}
            )
        }

        onNodeWithTag(SegmentFormSceneTestTag).assertDoesNotExist()
        onNodeWithTag(SegmentErrorSceneTestTag).assertIsDisplayed()
    }
}
