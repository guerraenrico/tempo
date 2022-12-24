package com.enricog.features.routines.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.emptyImmutableList
import com.enricog.core.compose.testing.invoke
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.ui_components.RoutinesEmptySceneTestTag
import com.enricog.features.routines.list.ui_components.RoutinesErrorSceneTestTag
import com.enricog.features.routines.list.ui_components.RoutinesSceneTestTag
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class RoutinesScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = RoutinesViewState.Idle

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderEmptySceneWhenStateIsEmpty() = composeRule {
        val viewState = RoutinesViewState.Empty

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutinesSceneWhenStateIsData() = composeRule {
        val viewState = RoutinesViewState.Data(routines = emptyImmutableList(), message = null)

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutinesErrorSceneWhenStateIsError() = composeRule {
        val exception = Exception("something went wrong")
        val viewState = RoutinesViewState.Error(throwable = exception)

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertIsDisplayed()
    }
}
