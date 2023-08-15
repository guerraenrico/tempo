package com.enricog.features.routines.detail.routine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.emptyImmutableList
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.testing.invoke
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.ui_components.RoutineErrorSceneTestTag
import com.enricog.features.routines.detail.routine.ui_components.RoutineFormSceneTestTag
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

class RoutineScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = RoutineViewState.Idle
        val inputs = RoutineInputs(
            name = "".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            preparationTime = "".timeText,
            frequencyGoal = RoutineInputs.FrequencyGoalInput.None
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    inputs = inputs,
                    onRoutineNameChange = {},
                    onRoutineRoundsChange = {},
                    onPreparationTimeChange = {},
                    onRoutineSave = {},
                    onPreparationTimeInfo = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {},
                    onFrequencyGoalCheck = {},
                    onFrequencyGoalPeriodChange = {},
                    onFrequencyGoalTimesChange = {}
                )
            }
        }

        onNodeWithTag(RoutineFormSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutineErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderSceneWhenStateIsData() = composeRule {
        val viewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = null,
            frequencyGoalItems = emptyImmutableList()
        )
        val inputs = RoutineInputs(
            name = "".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            preparationTime = "".timeText,
            frequencyGoal = RoutineInputs.FrequencyGoalInput.None
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    inputs = inputs,
                    onRoutineNameChange = {},
                    onRoutineRoundsChange = {},
                    onPreparationTimeChange = {},
                    onRoutineSave = {},
                    onPreparationTimeInfo = {},
                    onRetryLoad = {},
                    onSnackbarEvent = {},
                    onFrequencyGoalCheck = {},
                    onFrequencyGoalPeriodChange = {},
                    onFrequencyGoalTimesChange = {}
                )
            }
        }

        onNodeWithTag(RoutineFormSceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutineErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutineErrorSceneWhenStateIsError() = composeRule {
        val viewState = RoutineViewState.Error(throwable = Exception())
        val inputs = RoutineInputs(
            name = "".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            preparationTime = "".timeText,
            frequencyGoal = RoutineInputs.FrequencyGoalInput.None
        )

        setContent {
            viewState.Compose(
                inputs = inputs,
                onRoutineNameChange = {},
                onRoutineRoundsChange = {},
                onPreparationTimeChange = {},
                onRoutineSave = {},
                onPreparationTimeInfo = {},
                onRetryLoad = {},
                onSnackbarEvent = {},
                onFrequencyGoalCheck = {},
                onFrequencyGoalPeriodChange = {},
                onFrequencyGoalTimesChange = {}
            )
        }

        onNodeWithTag(RoutineFormSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutineErrorSceneTestTag).assertIsDisplayed()
    }
}
