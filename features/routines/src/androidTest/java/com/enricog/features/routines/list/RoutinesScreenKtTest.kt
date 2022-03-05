package com.enricog.features.routines.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.ui_components.EmptySceneTestTag
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
                    onCreateRoutineClick = {},
                    onRoutineClick = {},
                    onRoutineDelete = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(EmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderEmptySceneWhenStateIsEmpty() = composeRule {
        val viewState = RoutinesViewState.Empty

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutineClick = {},
                    onRoutineClick = {},
                    onRoutineDelete = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(EmptySceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutinesSceneWhenStateIsData() = composeRule {
        val viewState = RoutinesViewState.Data(emptyList())

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutineClick = {},
                    onRoutineClick = {},
                    onRoutineDelete = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(EmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertIsDisplayed()
    }
}
