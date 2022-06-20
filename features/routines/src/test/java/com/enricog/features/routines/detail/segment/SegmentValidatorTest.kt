package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Test
import kotlin.test.assertEquals

class SegmentValidatorTest {

    private val sut = SegmentValidator()

    @Test
    fun `should return error when segment name is blank`() {
        val inputs = SegmentInputs(
            name = "".toTextFieldValue(),
            time = "10".timeText,
            type = TimeType.TIMER
        )
        val expected = mapOf(SegmentField.Name to SegmentFieldError.BlankSegmentName)

        val actual = sut.validate(inputs = inputs)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return error when segment time is less or equal to zero and type is not TimeType#STOPWATCH`() {
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            time = "".timeText,
            type = TimeType.TIMER
        )
        val expected = mapOf<SegmentField, SegmentFieldError>(
            SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
        )

        val actual = sut.validate(inputs = inputs)

        assertEquals(expected, actual)
    }

    @Test
    fun `should not return error when segment time is less or equal to zero and type is TimeType#STOPWATCH`() {
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            time = "".timeText,
            type = TimeType.STOPWATCH
        )
        val expected = emptyMap<SegmentField, SegmentFieldError>()

        val actual = sut.validate(inputs = inputs)

        assertEquals(expected, actual)
    }
}
