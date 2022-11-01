package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action.SaveRoutineError
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Test
import kotlin.test.assertEquals

class RoutineReducerTest {

    private val sut = RoutineReducer()

    @Test
    fun `should setup state with routine`() {
        val routine = Routine.EMPTY.copy(
            name = "routine name",
            startTimeOffset = 50.seconds
        )
        val expected = RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "routine name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )

        val actual = sut.setup(routine = routine)

        assertEquals(expected, actual)
    }

    @Test
    fun `should setup error state`() {
        val exception = Exception()
        val expected = RoutineState.Error(throwable = exception)

        val actual = sut.error(throwable = exception)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update routine name and remove field name error`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(RoutineField.Name to RoutineFieldError.BlankRoutineName),
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )

        val actual = sut.updateRoutineName(state = state, textFieldValue = "name".toTextFieldValue())

        assertEquals(expected, actual)
    }

    @Test
    fun `should update routine start time offset`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "name".toTextFieldValue(),
                startTimeOffset = "51".timeText
            ),
            action = null
        )

        val actual = sut.updateRoutineStartTimeOffset(state = state, text = "51".timeText)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return same state when seconds a more than max`() {
        val expected = RoutineState.Data(
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

        val actual = sut.updateRoutineStartTimeOffset(
            state = expected,
            text = "61".timeText
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `should apply routine errors`() {
        val errors = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName
        )
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = errors,
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )

        val actual = sut.applyRoutineErrors(state = state, errors = errors)

        assertEquals(expected, actual)
    }

    @Test
    fun `should apply routine error action when save throws an error`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = SaveRoutineError
        )

        val actual = sut.saveRoutineError(state = state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should reset action when it is handled`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = SaveRoutineError
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            action = null
        )

        val actual = sut.actionHandled(state = state)

        assertEquals(expected, actual)
    }
}
