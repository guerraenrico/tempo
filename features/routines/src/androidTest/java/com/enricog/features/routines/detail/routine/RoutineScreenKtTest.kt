package com.enricog.features.routines.detail.routine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.ui_components.RoutineFormSceneTestTag
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
                    onRoutineSave = {}
                )
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
                    onRoutineSave = {}
                )
            }
        }

        onNodeWithTag(RoutineFormSceneTestTag).assertIsDisplayed()
    }
}
