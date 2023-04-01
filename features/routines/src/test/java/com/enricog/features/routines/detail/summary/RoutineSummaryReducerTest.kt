package com.enricog.features.routines.detail.summary

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.ID
import com.enricog.core.entities.asID
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineSummaryReducerTest {

    private val reducer = RoutineSummaryReducer()

    @Test
    fun `should setup data state`() {
        val routine = Routine.EMPTY
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryState.Data(
            routine = routine,
            errors = emptyMap(),
            action = null
        )

        val actual = reducer.setup(state = state, routine = routine)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.setup(state = state, routine = routine)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should set error state`() {
        val exception = Exception("Something went wrong")
        val expected = RoutineSummaryState.Error(exception)

        val actual = reducer.error(throwable = exception)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.applyRoutineErrors(state = state, errors = errors)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.deleteSegmentError(state = state, segmentId = segmentId)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.deleteSegmentSuccess(state = state)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.moveSegmentError(state = state)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.duplicateSegmentError(state = state)

        assertThat(actual).isEqualTo(expected)
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

        val actual = reducer.actionHandled(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}
