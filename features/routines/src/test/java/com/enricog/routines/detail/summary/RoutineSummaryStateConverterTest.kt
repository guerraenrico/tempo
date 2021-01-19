package com.enricog.routines.detail.summary

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryField
import com.enricog.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineSummaryStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = RoutineSummaryStateConverter()

    @Test
    fun `should convert Idle state to Idle viewState`() = coroutineRule {
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `should convert Data state to Data viewState`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments)
        )
        val expected = RoutineSummaryViewState.Data(
            routine = Routine.EMPTY,
            errors = mapOf(RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments)
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }
}