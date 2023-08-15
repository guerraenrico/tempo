package com.enricog.features.routines.detail.segment

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.testing.invoke
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.ui_components.SegmentErrorSceneTestTag
import com.enricog.features.routines.detail.segment.ui_components.SegmentFormSceneTestTag
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
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
        val inputs = SegmentInputs(
            name = "".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            time = "".timeText,
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    inputs = inputs,
                    onSegmentNameChange = {},
                    onSegmentRoundsChange = {},
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
        val timerTheme = TimerTheme.DEFAULT
        val viewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(
                timeType = TimeTypeEntity.TIMER,
                timerTheme = timerTheme
            ),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val inputs = SegmentInputs(
            name = "".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            time = "".timeText,
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    inputs = inputs,
                    onSegmentNameChange = {},
                    onSegmentRoundsChange = {},
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
        val inputs = SegmentInputs(
            name = "".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            time = "".timeText,
        )

        setContent {
            viewState.Compose(
                inputs = inputs,
                onSegmentNameChange = {},
                onSegmentRoundsChange = {},
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
