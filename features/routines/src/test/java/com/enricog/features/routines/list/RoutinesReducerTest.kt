package com.enricog.features.routines.list

import com.enricog.core.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DuplicateRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutinesReducerTest {

    private val routinesReducer = RoutinesReducer()

    @Test
    fun `setup should return empty state when routine list is empty`() {
        val routines = emptyList<Routine>()
        val timerTheme = TimerTheme.DEFAULT
        val state = RoutinesState.Data(routines = listOf(Routine.EMPTY), action = null, timerTheme = timerTheme)
        val expected = RoutinesState.Empty

        val actual = routinesReducer.setup(state = state, routines = routines, timerTheme = timerTheme)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `setup should return data state when routine list is not empty`() {
        val routines = listOf(Routine.EMPTY)
        val state = RoutinesState.Empty
        val timerTheme = TimerTheme.DEFAULT
        val expected = RoutinesState.Data(routines = routines, action = null, timerTheme = timerTheme)

        val actual = routinesReducer.setup(state = state, routines = routines, timerTheme = timerTheme)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `setup should return data state updating only routines when state is data`() {
        val routines = listOf(Routine.EMPTY)
        val timerTheme = TimerTheme.DEFAULT
        val state = RoutinesState.Data(
            routines = listOf(Routine.EMPTY, Routine.EMPTY),
            action = DuplicateRoutineError,
            timerTheme = timerTheme
        )
        val expected = RoutinesState.Data(routines = routines, action = DuplicateRoutineError, timerTheme = timerTheme)

        val actual = routinesReducer.setup(state = state, routines = routines, timerTheme = timerTheme)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `error should return error state`() {
        val exception = Exception("something went wrong")
        val expected = RoutinesState.Error(throwable = exception)

        val actual = routinesReducer.error(throwable = exception)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply delete routine error action`() {
        val routineId = 1.asID
        val routine = Routine.EMPTY.copy(id = routineId)
        val routines = listOf(routine)
        val timerTheme = TimerTheme.DEFAULT
        val state = RoutinesState.Data(routines = routines, action = null, timerTheme = timerTheme)
        val expected = RoutinesState.Data(
            routines = routines,
            action = DeleteRoutineError(routineId = routineId),
            timerTheme = timerTheme
        )

        val actual = routinesReducer.deleteRoutineError(state = state, routineId = routineId)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply move routine error action`() {
        val routineId = 1.asID
        val routine = Routine.EMPTY.copy(id = routineId)
        val routines = listOf(routine)
        val timerTheme = TimerTheme.DEFAULT
        val state = RoutinesState.Data(routines = routines, action = null, timerTheme = timerTheme)
        val expected = RoutinesState.Data(
            routines = routines,
            action = MoveRoutineError,
            timerTheme = timerTheme
        )

        val actual = routinesReducer.moveRoutineError(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply duplicate routine error action`() {
        val routineId = 1.asID
        val routine = Routine.EMPTY.copy(id = routineId)
        val routines = listOf(routine)
        val timerTheme = TimerTheme.DEFAULT
        val state = RoutinesState.Data(routines = routines, action = null, timerTheme = timerTheme)
        val expected = RoutinesState.Data(
            routines = routines,
            action = DuplicateRoutineError,
            timerTheme = timerTheme
        )

        val actual = routinesReducer.duplicateRoutineError(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should set state without action when action is handled`() {
        val routineId = 1.asID
        val routine = Routine.EMPTY.copy(id = routineId)
        val routines = listOf(routine)
        val timerTheme = TimerTheme.DEFAULT
        val state = RoutinesState.Data(
            routines = routines,
            action = DeleteRoutineError(routineId = routineId),
            timerTheme = timerTheme
        )
        val expected = RoutinesState.Data(routines = routines, action = null, timerTheme = timerTheme)

        val actual = routinesReducer.actionHandled(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}
