package com.enricog.features.routines.detail.summary

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import org.junit.Test
import kotlin.test.assertEquals

class RoutineSummaryReducerTest {

    private val sut = RoutineSummaryReducer()

    @Test
    fun `should setup data state`() {
        val routine = Routine.EMPTY
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryState.Data(
            routine = routine,
            errors = emptyMap(),
            action = null
        )

        val result = sut.setup(state = state, routine = routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should setup state updating the routine when state is data`() {
        val routine = Routine.EMPTY.copy(id = ID.from(value = 2))
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = ID.from(value = 1)),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DeleteSegmentSuccess
        )
        val expected = RoutineSummaryState.Data(
            routine = routine,
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DeleteSegmentSuccess
        )

        val result = sut.setup(state = state, routine = routine)

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
        val segmentId = 1.asID
        val segment = Segment.EMPTY.copy(id = segmentId)
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
            action = Action.DeleteSegmentError(segmentId = segmentId)
        )

        val result = sut.deleteSegmentError(state = state, segmentId = segmentId)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply delete segment success action`() {
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
            action = Action.DeleteSegmentSuccess
        )

        val result = sut.deleteSegmentSuccess(state = state)

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

        val result = sut.moveSegmentError(state = state)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply duplicate segment error action`() {
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
            action = Action.DuplicateSegmentError
        )

        val result = sut.duplicateSegmentError(state = state)

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
