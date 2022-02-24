package com.enricog.features.routines.list

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesViewState
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

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