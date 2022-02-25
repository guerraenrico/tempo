package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.testing.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentViewState
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
