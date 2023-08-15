package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.test.RoutineInputs
import com.enricog.features.routines.detail.routine.test.frequencyGoalDropDownItems
import com.enricog.ui.components.extensions.toTextFieldValue
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineValidatorTest {

    private val validator = RoutineValidator()

    @Test
    fun `should return error when routine name is blank`() {
        val inputs = RoutineInputs {
            name = "".toTextFieldValue()
        }
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when routine rounds is blank`() {
        val inputs = RoutineInputs {
            rounds = "".toTextFieldValue()
        }
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Rounds to RoutineFieldError.BlankRoutineRounds,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when routine rounds is zero`() {
        val inputs = RoutineInputs {
            rounds = "0".toTextFieldValue()
        }
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Rounds to RoutineFieldError.BlankRoutineRounds,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when routine frequency goal times is zero`() {
        val inputs = RoutineInputs {
            RoutineInputs.FrequencyGoalInput.Value(
                frequencyGoalTimes = "0".toTextFieldValue(),
                frequencyGoalPeriod = frequencyGoalDropDownItems.getValue(FrequencyGoal.Period.DAY)
            )
        }
        val expected: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Rounds to RoutineFieldError.BlankRoutineRounds,
        )

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }
}
