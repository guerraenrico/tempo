package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.testing.EMPTY
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import kotlin.test.assertEquals
import org.junit.Test

class SegmentValidatorTest {

    private val sut = SegmentValidator()

    @Test
    fun `should return error when segment name is blank`() {
        val segment = Segment.EMPTY.copy(
            name = "",
            time = 10.seconds,
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
            time = 0.seconds,
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
            time = 0.seconds,
            type = TimeType.STOPWATCH
        )
        val expected = emptyMap<SegmentField, SegmentFieldError>()

        val result = sut.validate(segment = segment)

        assertEquals(expected, result)
    }
}
