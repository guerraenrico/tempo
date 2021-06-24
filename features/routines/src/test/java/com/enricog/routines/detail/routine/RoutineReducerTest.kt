package com.enricog.routines.detail.routine

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.seconds
import com.enricog.routines.detail.routine.models.RoutineField
import com.enricog.routines.detail.routine.models.RoutineFieldError
import com.enricog.routines.detail.routine.models.RoutineState
import kotlin.test.assertEquals
import org.junit.Test

class RoutineReducerTest {

    private val sut = RoutineReducer()

    @Test
    fun `should set routine`() {
        val routine = Routine.EMPTY
        val expected = RoutineState.Data(routine = routine, errors = emptyMap())

        val result = sut.setup(routine = routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should update routine name and remove field name error`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY.copy(name = ""),
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName,
                RoutineField.StartTimeOffsetInSeconds to RoutineFieldError.InvalidRoutineStartTimeOffset
            )
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY.copy(name = "name"),
            errors = mapOf(
                RoutineField.StartTimeOffsetInSeconds to RoutineFieldError.InvalidRoutineStartTimeOffset
            )
        )

        val result = sut.updateRoutineName(state = state, text = "name")

        assertEquals(expected, result)
    }

    @Test
    fun `should update routine start time offset and remove field start time offset error`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY.copy(startTimeOffset = 0.seconds),
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName,
                RoutineField.StartTimeOffsetInSeconds to RoutineFieldError.InvalidRoutineStartTimeOffset
            )
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY.copy(startTimeOffset = 10.seconds),
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName,
            )
        )

        val result = sut.updateRoutineStartTimeOffset(state = state, seconds = 10.seconds)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply routine errors`() {
        val errors = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName,
            RoutineField.StartTimeOffsetInSeconds to RoutineFieldError.InvalidRoutineStartTimeOffset
        )
        val state = RoutineState.Data(
            routine = Routine.EMPTY.copy(name = ""),
            errors = emptyMap()
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY.copy(name = ""),
            errors = errors
        )

        val result = sut.applyRoutineErrors(state = state, errors = errors)

        assertEquals(expected, result)
    }
}
