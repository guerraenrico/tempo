package com.enricog.features.routines.list

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import org.junit.Test
import kotlin.test.assertEquals

class RoutinesReducerTest {

    private val sut = RoutinesReducer()

    @Test
    fun `setup should return empty state when routine list is empty`() {
        val routines = emptyList<Routine>()
        val expected = RoutinesState.Empty

        val result = sut.setup(routines)

        assertEquals(expected, result)
    }

    @Test
    fun `setup should return data state when routine list is not empty`() {
        val routines = listOf(Routine.EMPTY)
        val expected = RoutinesState.Data(routines = routines, action = null)

        val result = sut.setup(routines)

        assertEquals(expected, result)
    }

    @Test
    fun `error should return error state`() {
        val exception = Exception("something went wrong")
        val expected = RoutinesState.Error(throwable = exception)

        val result = sut.error(throwable = exception)

        assertEquals(expected, result)
    }

    @Test
    fun `should set state with delete routine error action`() {
        val routine = Routine.EMPTY
        val routines = listOf(routine)
        val state = RoutinesState.Data(routines = routines, action = null)
        val expected = RoutinesState.Data(routines = routines, action = DeleteRoutineError(routine))

        val result = sut.deleteRoutineError(state = state, routine = routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should set state without action when action is handled`() {
        val routine = Routine.EMPTY
        val routines = listOf(routine)
        val state = RoutinesState.Data(routines = routines, action = DeleteRoutineError(routine))
        val expected = RoutinesState.Data(routines = routines, action = null)

        val result = sut.actionHandled(state = state)

        assertEquals(expected, result)
    }
}
