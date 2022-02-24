package com.enricog.features.routines.detail.routine

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.entities.routines.Routine
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

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
            )
        )
        val expected = RoutineViewState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(
                RoutineField.Name to R.string.field_error_message_routine_name_blank,
            )
        )

        val result = sut.convert(state = state)

        assertEquals(expected, result)
    }
}
