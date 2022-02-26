package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import org.junit.Test
import kotlin.test.assertEquals

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
}
