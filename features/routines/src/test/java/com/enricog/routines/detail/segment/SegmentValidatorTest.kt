package com.enricog.routines.detail.segment

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.segment.models.SegmentField
import com.enricog.routines.detail.segment.models.SegmentFieldError
import kotlin.test.assertEquals
import org.junit.Test

class SegmentValidatorTest {

    private val sut = SegmentValidator()

    @Test
    fun `should return error when segment name is blank`() {
        val segment = Segment.EMPTY.copy(
            name = "",
            timeInSeconds = 10,
            type = TimeType.TIMER
        )
        val expected = mapOf<SegmentField, SegmentFieldError>(
            SegmentField.Name to SegmentFieldError.BlankSegmentName
        )

        val result = sut.validate(segment = segment)

        assertEquals(expected, result)
    }

    @Test
    fun `should return error when segment time is less or equal to zero and type is not TimeType#STOPWATCH`() {
        val segment = Segment.EMPTY.copy(
            name = "name",
            timeInSeconds = 0,
            type = TimeType.TIMER
        )
        val expected = mapOf<SegmentField, SegmentFieldError>(
            SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
        )

        val result = sut.validate(segment = segment)

        assertEquals(expected, result)
    }

    @Test
    fun `should not return error when segment time is less or equal to zero and type is TimeType#STOPWATCH`() {
        val segment = Segment.EMPTY.copy(
            name = "name",
            timeInSeconds = 0,
            type = TimeType.STOPWATCH
        )
        val expected = emptyMap<SegmentField, SegmentFieldError>()

        val result = sut.validate(segment = segment)

        assertEquals(expected, result)
    }
}
