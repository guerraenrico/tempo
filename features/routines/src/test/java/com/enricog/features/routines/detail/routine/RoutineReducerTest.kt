package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.ui.components.textField.timeText
import org.junit.Test
import kotlin.test.assertEquals

class RoutineReducerTest {

    private val sut = RoutineReducer()

    @Test
    fun `should set routine`() {
        val routine = Routine.EMPTY.copy(
            name = "routine name",
            startTimeOffset = 50.seconds
        )
        val expected = RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "routine name",
                startTimeOffset = "50".timeText
            )
        )

        val actual = sut.setup(routine = routine)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update routine name and remove field name error`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(RoutineField.Name to RoutineFieldError.BlankRoutineName),
            inputs = RoutineInputs(
                name = "",
                startTimeOffset = "50".timeText
            )
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "name",
                startTimeOffset = "50".timeText
            )
        )

        val actual = sut.updateRoutineName(state = state, text = "name")

        assertEquals(expected, actual)
    }

    @Test
    fun `should update routine start time offset`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "name",
                startTimeOffset = "50".timeText
            )
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            inputs = RoutineInputs(
                name = "name",
                startTimeOffset = "51".timeText
            )
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
                name = "name",
                startTimeOffset = "50".timeText
            )
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
                name = "",
                startTimeOffset = "50".timeText
            )
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = errors,
            inputs = RoutineInputs(
                name = "",
                startTimeOffset = "50".timeText
            )
        )

        val actual = sut.applyRoutineErrors(state = state, errors = errors)

        assertEquals(expected, actual)
    }
}
