package com.enricog.routines.list

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.routines.list.models.RoutinesState
import org.junit.Test
import kotlin.test.assertEquals

class RoutinesReducerTest {

    private val sut = RoutinesReducer()

    @Test
    fun `test setup should return empty state when routine list is empty`() {
        val routines = emptyList<Routine>()
        val expected = RoutinesState.Empty

        val result = sut.setup(routines)

        assertEquals(expected, result)
    }

    @Test
    fun `test setup should return data state when routine list is not empty`() {
        val routines = listOf(Routine.EMPTY)
        val expected = RoutinesState.Data(routines)

        val result = sut.setup(routines)

        assertEquals(expected, result)
    }

    @Test
    fun `test deleteRoutine should remove routine from routines`() {
        val state = RoutinesState.Data(
            routines = listOf(
                Routine.EMPTY.copy(id = 1),
                Routine.EMPTY.copy(id = 2)
            )
        )
        val expected = RoutinesState.Data(
            routines = listOf(
                Routine.EMPTY.copy(id = 1)
            )
        )

        val result = sut.deleteRoutine(state, Routine.EMPTY.copy(id = 2))

        assertEquals(expected, result)
    }

    @Test
    fun `test deleteRoutine should remove routine from routines and return empty state when there are no other routines`() {
        val state = RoutinesState.Data(
            routines = listOf(
                Routine.EMPTY.copy(id = 1),
            )
        )
        val expected = RoutinesState.Empty

        val result = sut.deleteRoutine(state, Routine.EMPTY.copy(id = 1))

        assertEquals(expected, result)
    }

}