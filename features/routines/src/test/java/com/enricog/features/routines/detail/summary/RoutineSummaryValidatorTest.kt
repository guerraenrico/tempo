package com.enricog.features.routines.detail.summary

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.google.common.truth.Truth
import org.junit.Test

class RoutineSummaryValidatorTest {

    private val validator = RoutineSummaryValidator()

    @Test
    fun `should return error no segments when there are no segments in routine`() {
        val routine = Routine.EMPTY.copy(segments = emptyList())
        val expected = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )

        val actual = validator.validate(routine)

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return no error no segments when the routine is valid`() {
        val routine = Routine.EMPTY.copy(segments = listOf(Segment.EMPTY))
        val expected = emptyMap<RoutineSummaryField, RoutineSummaryFieldError>()

        val actual = validator.validate(routine)

        Truth.assertThat(actual).isEqualTo(expected)
    }
}
