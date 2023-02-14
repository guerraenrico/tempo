package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action.SaveRoutineError
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineReducerTest {

    private val reducer = RoutineReducer()

    @Test
    fun `should setup state with routine`() {
        val routine = Routine.EMPTY.copy(
            name = "routine name",
            preparationTime = 50.seconds
        )
        val expected = RoutineState.Data(
            routine = routine,
            errors = emptyMap(),
            action = null
        )

        val actual = reducer.setup(routine = routine)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should setup error state`() {
        val exception = Exception()
        val expected = RoutineState.Error(throwable = exception)

        val actual = reducer.error(throwable = exception)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should remove field name error when routine name is updated`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(RoutineField.Name to RoutineFieldError.BlankRoutineName),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            action = null
        )

        val actual = reducer.updateRoutineName(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply routine errors`() {
        val errors = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName
        )
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = errors,
            action = null
        )

        val actual = reducer.applyRoutineErrors(state = state, errors = errors)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply routine error action when save throws an error`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            action = null
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            action = SaveRoutineError
        )

        val actual = reducer.saveRoutineError(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should reset action when it is handled`() {
        val state = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            action = SaveRoutineError
        )
        val expected = RoutineState.Data(
            routine = Routine.EMPTY,
            errors = emptyMap(),
            action = null
        )

        val actual = reducer.actionHandled(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}
