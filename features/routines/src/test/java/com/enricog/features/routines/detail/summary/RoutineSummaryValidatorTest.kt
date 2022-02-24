package com.enricog.features.routines.detail.summary

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import kotlin.test.assertEquals
import org.junit.Test

class RoutineSummaryValidatorTest {

    private val sut = RoutineSummaryValidator()

    @Test
    fun `should return error no segments when there are no segments in routine`() {
        val routine = Routine.EMPTY.copy(segments = emptyList())
        val expected = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )

        val result = sut.validate(routine)

        assertEquals(expected, result)
    }

    @Test
    fun `should return no error no segments when the routine is valid`() {
        val routine = Routine.EMPTY.copy(segments = listOf(Segment.EMPTY))
        val expected = emptyMap<RoutineSummaryField, RoutineSummaryFieldError>()

        val result = sut.validate(routine)

        assertEquals(expected, result)
    }
}
