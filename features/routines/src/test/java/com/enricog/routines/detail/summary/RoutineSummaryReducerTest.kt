package com.enricog.routines.detail.summary

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryField
import com.enricog.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import kotlin.test.assertEquals
import org.junit.Test

class RoutineSummaryReducerTest {

    private val sut = RoutineSummaryReducer()

    @Test
    fun `should setup Data state`() {
        val routine = Routine.EMPTY
        val expected = RoutineSummaryState.Data(routine = routine, errors = emptyMap())

        val result = sut.setup(routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should remove segment routine in state`() {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 1),
                    Segment.EMPTY.copy(id = 2)
                )
            ),
            errors = emptyMap()
        )
        val expected = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 2)
                )
            ),
            errors = emptyMap()
        )

        val result = sut.deleteSegment(state, Segment.EMPTY.copy(id = 1))

        assertEquals(expected, result)
    }

    @Test
    fun `should apply segment errors in state`() {
        val errors = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 1))
            ),
            errors = emptyMap()
        )
        val expected = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 1))
            ),
            errors = errors
        )

        val result = sut.applyRoutineErrors(state, errors)

        assertEquals(expected, result)
    }
}
