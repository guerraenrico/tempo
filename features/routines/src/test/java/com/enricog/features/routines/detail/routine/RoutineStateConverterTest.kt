package com.enricog.features.routines.detail.routine

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineFields
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = RoutineStateConverter()

    @Test
    fun `test map RoutineState#Idle`() = coroutineRule {
        val state = RoutineState.Idle
        val expected = RoutineViewState.Idle

        val result = sut.convert(state = state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map RoutineState#Data`() = coroutineRule {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName,
            ),
            inputs = RoutineInputs(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            )
        )
        val expected = RoutineViewState.Data(
            routine = RoutineFields(
                name = "name".toTextFieldValue(),
                startTimeOffset = "50".timeText
            ),
            errors = mapOf(
                RoutineField.Name to RoutineFieldError.BlankRoutineName
            )
        )

        val result = sut.convert(state = state)

        assertEquals(expected, result)
    }
}
