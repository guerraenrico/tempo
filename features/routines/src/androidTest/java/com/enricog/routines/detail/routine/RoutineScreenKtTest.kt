package com.enricog.routines.detail.routine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.routines.detail.routine.models.RoutineViewState
import com.enricog.routines.detail.routine.ui_components.RoutineFormSceneTestTag
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class RoutineScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = RoutineViewState.Idle

        setContent {
            TempoTheme {
                viewState.Compose(
                    onRoutineNameChange = {},
                    onStartTimeOffsetChange = {},
                    onRoutineSave = {})
            }
        }

        onNodeWithTag(RoutineFormSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = RoutineViewState.Data(routine = Routine.EMPTY, errors = emptyMap())

        setContent {
            TempoTheme {
                viewState.Compose(
                    onRoutineNameChange = {},
                    onStartTimeOffsetChange = {},
                    onRoutineSave = {})
            }
        }

        onNodeWithTag(RoutineFormSceneTestTag).assertIsDisplayed()
    }
}