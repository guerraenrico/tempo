package com.enricog.routines.detail.routine

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.seconds
import com.enricog.routines.detail.routine.models.RoutineField
import com.enricog.routines.detail.routine.models.RoutineFieldError
import kotlin.test.assertEquals
import org.junit.Test

class RoutineValidatorTest {

    private val sut = RoutineValidator()

    @Test
    fun `should return error when routine name is blank`() {
        val routine = Routine.EMPTY.copy(name = "", startTimeOffset = 0.seconds)
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName,
        )

        val result = sut.validate(routine = routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should return error when routine startTimeOffsetInSeconds is less than 0`() {
        val routine = Routine.EMPTY.copy(name = "name", startTimeOffset = (-1).seconds)
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.StartTimeOffsetInSeconds to RoutineFieldError.InvalidRoutineStartTimeOffset,
        )

        val result = sut.validate(routine = routine)

        assertEquals(expected, result)
    }
}
