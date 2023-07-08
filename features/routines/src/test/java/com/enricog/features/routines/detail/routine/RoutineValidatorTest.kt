package com.enricog.features.routines.detail.routine

import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineValidatorTest {

    private val validator = RoutineValidator()

    @Test
    fun `should return error when routine name is blank`() {
        val inputs = RoutineInputs(
            name = "".toTextFieldValue(),
            rounds = "1".toTextFieldValue(),
            preparationTime = "".timeText
        )
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when routine rounds is blank`() {
        val inputs = RoutineInputs(
            name = "Routine Name".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            preparationTime = "".timeText
        )
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Rounds to RoutineFieldError.BlankRoutineRounds,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when routine rounds is zero`() {
        val inputs = RoutineInputs(
            name = "Routine Name".toTextFieldValue(),
            rounds = "0".toTextFieldValue(),
            preparationTime = "".timeText
        )
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Rounds to RoutineFieldError.BlankRoutineRounds,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }
}
