package com.enricog.features.routines.detail.summary

import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import kotlin.test.assertEquals
import org.junit.Test

class RoutineSummaryReducerTest {

    private val sut = RoutineSummaryReducer()

    @Test
    fun `should setup data state`() {
        val routine = Routine.EMPTY
        val expected = RoutineSummaryState.Data(
            routine = routine,
            errors = emptyMap(),
            action = null
        )

        val result = sut.setup(routine = routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should set error state`() {
        val exception = Exception("Something went wrong")
        val expected = RoutineSummaryState.Error(exception)

        val result = sut.error(throwable = exception)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply segment errors in state`() {
        val errors = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 1.asID))
            ),
            errors = emptyMap(),
            action = null
        )
        val expected = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 1.asID))
            ),
            errors = errors,
            action = null
        )

        val result = sut.applyRoutineErrors(state = state, errors = errors)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply delete segment error action`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(segment)
            ),
            errors = emptyMap(),
            action = null
        )
        val expected = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(segment)
            ),
            errors = emptyMap(),
            action = Action.DeleteSegmentError(segment = segment)
        )

        val result = sut.deleteSegmentError(state = state, segment = segment)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply move segment error action`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(segment)
            ),
            errors = emptyMap(),
            action = null
        )
        val expected = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(segment)
            ),
            errors = emptyMap(),
            action = Action.MoveSegmentError
        )

        val result = sut.segmentMoveError(state = state)

        assertEquals(expected, result)
    }

    @Test
    fun `should cleanup action when handled`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(segment)
            ),
            errors = emptyMap(),
            action = Action.MoveSegmentError
        )
        val expected = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(segment)
            ),
            errors = emptyMap(),
            action = null
        )

        val result = sut.actionHandled(state = state)

        assertEquals(expected, result)
    }
}
