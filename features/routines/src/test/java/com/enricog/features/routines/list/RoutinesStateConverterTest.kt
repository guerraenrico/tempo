package com.enricog.features.routines.list

import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import com.enricog.data.routines.api.entities.Routine.Companion as RoutineEntity

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
        val routineEntity = RoutineEntity.EMPTY
        val routineItem = RoutinesItem.RoutineItem(id = 0.asID, name = "", rank = "aaaaaa")
        val state = RoutinesState.Data(routines = listOf(routineEntity), action = null)
        val viewState = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = null
        )

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }

    @Test
    fun `test map data with delete routine error action`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY
        val routineItem = RoutinesItem.RoutineItem(id = 0.asID, name = "", rank = "aaaaaa")
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DeleteRoutineError(routineEntity.id)
        )
        val viewState = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )
        )

        val result = sut.convert(state)

        assertEquals(viewState, result)
    }

    @Test
    fun `test map data with move routine error action`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY
        val routineItem = RoutinesItem.RoutineItem(id = 0.asID, name = "", rank = "aaaaaa")
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.MoveRoutineError
        )
        val viewState = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = Message(
                textResId = R.string.label_routines_move_error,
                actionTextResId = null
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
