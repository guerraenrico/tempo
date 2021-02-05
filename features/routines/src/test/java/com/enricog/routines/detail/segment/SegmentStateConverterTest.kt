package com.enricog.routines.detail.segment

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.routines.detail.segment.models.SegmentField
import com.enricog.routines.detail.segment.models.SegmentFieldError
import com.enricog.routines.detail.segment.models.SegmentState
import com.enricog.routines.detail.segment.models.SegmentViewState
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class SegmentStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = SegmentStateConverter()

    @Test
    fun `should map SegmentState#Idle`() = coroutineRule {
        val state = SegmentState.Idle
        val expected = SegmentViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `should map SegmentState#Data`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyList()
        )
        val expected = SegmentViewState.Data(
            segment = Segment.EMPTY,
            errors = mapOf(
                SegmentField.Name to R.string.field_error_message_segment_name_blank,
                SegmentField.TimeInSeconds to R.string.field_error_message_segment_time_invalid
            ),
            timeTypes = emptyList()
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }
}
