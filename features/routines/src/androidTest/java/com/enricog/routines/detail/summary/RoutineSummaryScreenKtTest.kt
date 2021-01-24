package com.enricog.routines.detail.summary

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.ui_components.RoutineSummarySceneTestTag
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class RoutineSummaryScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = RoutineSummaryViewState.Idle

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onRoutineStart = {},
                    onRoutineEdit = {})
            }
        }

        waitForIdle()

        onNodeWithTag(RoutineSummarySceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutineSummarySceneWhenStateIsData() = composeRule {
        val viewState = RoutineSummaryViewState.Data(items = emptyList())

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onRoutineStart = {},
                    onRoutineEdit = {})
            }
        }

        waitForIdle()

        onNodeWithTag(RoutineSummarySceneTestTag).assertExists()
    }
}