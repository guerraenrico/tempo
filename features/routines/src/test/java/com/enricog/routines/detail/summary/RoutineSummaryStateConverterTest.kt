package com.enricog.routines.detail.summary

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryField
import com.enricog.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class RoutineSummaryStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = RoutineSummaryStateConverter()

    @Test
    fun `test map  RoutineSummaryState#Idle`() = coroutineRule {
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map  RoutineSummaryState#Data`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1),
                    Segment.EMPTY.copy(id = 2),
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments)
        )
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 1)),
                RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 2)),
                RoutineSummaryItem.Space
            )
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }
}
