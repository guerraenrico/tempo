package com.enricog.features.routines.list

import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.list.models.RoutinesState
import kotlin.test.assertEquals
import org.junit.Test

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
}
