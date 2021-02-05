package com.enricog.routines.list

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class RoutinesStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = RoutinesStateConverter()

    @Test
    fun `test map idle`() = coroutineRule {
        val state = RoutinesState.Idle
        val viewState = RoutinesViewState.Idle

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }

    @Test
    fun `test map empty`() = coroutineRule {
        val state = RoutinesState.Empty
        val viewState = RoutinesViewState.Empty

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }

    @Test
    fun `test map data`() = coroutineRule {
        val state = RoutinesState.Data(routines = emptyList())
        val viewState = RoutinesViewState.Data(routines = emptyList())

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }
}
