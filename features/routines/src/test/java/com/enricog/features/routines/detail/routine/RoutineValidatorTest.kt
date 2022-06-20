package com.enricog.features.routines.detail.routine

import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Test
import kotlin.test.assertEquals

class RoutineValidatorTest {

    private val sut = RoutineValidator()

    @Test
    fun `should return error when routine name is blank`() {
        val inputs = RoutineInputs(
            name = "".toTextFieldValue(),
            startTimeOffset = "".timeText
        )
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName,
        )

        val result = sut.validate(inputs = inputs)

        assertEquals(expected, result)
    }
}
