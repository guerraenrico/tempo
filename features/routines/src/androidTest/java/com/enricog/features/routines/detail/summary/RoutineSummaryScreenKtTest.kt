package com.enricog.features.routines.detail.summary

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.emptyImmutableList
import com.enricog.core.compose.testing.invoke
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.ui_components.RoutineSummaryErrorSceneTag
import com.enricog.features.routines.detail.summary.ui_components.RoutineSummarySceneTestTag
import com.enricog.ui.theme.TempoTheme
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
                    onRoutineEdit = {},
                    onSegmentMoved = { _, _ -> },
                    onSnackbarEvent = {},
                    onRetryLoad = {}
                )
            }
        }

        onNodeWithTag(RoutineSummarySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutineSummaryErrorSceneTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutineSummarySceneWhenStateIsData() = composeRule {
        val viewState = RoutineSummaryViewState.Data(items = emptyImmutableList(), message = null)

        setContent {
            TempoTheme {
                viewState.Compose(
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onRoutineStart = {},
                    onRoutineEdit = {},
                    onSegmentMoved = { _, _ -> },
                    onSnackbarEvent = {},
                    onRetryLoad = {}
                )
            }
        }

        onNodeWithTag(RoutineSummarySceneTestTag).assertExists()
        onNodeWithTag(RoutineSummaryErrorSceneTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutineSummaryErrorSceneWhenStateIsError() = composeRule {
        val viewState = RoutineSummaryViewState.Error(throwable = Exception())

        setContent {
            viewState.Compose(
                onSegmentAdd = {},
                onSegmentSelected = {},
                onSegmentDelete = {},
                onRoutineStart = {},
                onRoutineEdit = {},
                onSegmentMoved = { _, _ -> },
                onSnackbarEvent = {},
                onRetryLoad = {}
            )
        }

        onNodeWithTag(RoutineSummarySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutineSummaryErrorSceneTag).assertIsDisplayed()
    }
}
