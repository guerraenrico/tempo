package com.enricog.features.routines.list

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
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
    fun `test map data with no action`() = coroutineRule {
        val state = RoutinesState.Data(routines = emptyList(), action = null)
        val viewState = RoutinesViewState.Data(routines = emptyList(), message = null)

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }

    @Test
    fun `test map data with delete routine error action`() = coroutineRule {
        val routine = Routine.EMPTY
        val state = RoutinesState.Data(
            routines = listOf(routine),
            action = Action.DeleteRoutineError(routine)
        )
        val viewState = RoutinesViewState.Data(
            routines = listOf(routine),
            message = Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )
        )

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }

    @Test
    fun `test map error`() = coroutineRule {
        val exception = Exception("something went wrong")
        val state = RoutinesState.Error(throwable = exception)
        val viewState = RoutinesViewState.Error(throwable = exception)

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }
}
