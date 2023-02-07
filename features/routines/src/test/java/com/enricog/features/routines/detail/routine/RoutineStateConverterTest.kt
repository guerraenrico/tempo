package com.enricog.features.routines.detail.routine

import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action.SaveRoutineError
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class RoutineStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val stateConverter = RoutineStateConverter()

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = RoutineState.Idle
        val expected = RoutineViewState.Idle

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = RoutineState.Error(exception)
        val expected = RoutineViewState.Error(exception)

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state without action`() = coroutineRule {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName,
            ),
            inputs = RoutineInputs(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )
        val expected = RoutineViewState.Data(
            routine = RoutineFields(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            errors = immutableMapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName
            ),
            message = null
        )

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with action`() = coroutineRule {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName,
            ),
            inputs = RoutineInputs(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = SaveRoutineError
        )
        val expected = RoutineViewState.Data(
            routine = RoutineFields(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            errors = immutableMapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName
            ),
            message = RoutineViewState.Data.Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        )

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}
