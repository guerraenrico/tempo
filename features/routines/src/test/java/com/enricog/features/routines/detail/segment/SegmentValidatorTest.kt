package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SegmentValidatorTest {

    private val validator = SegmentValidator()

    @Test
    fun `should return error when segment name is blank`() {
        val inputs = SegmentInputs(
            name = "".toTextFieldValue(),
            time = "10".timeText,
            type = TimeType.TIMER
        )
        val expected = mapOf(SegmentField.Name to SegmentFieldError.BlankSegmentName)

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when segment time is less or equal to zero and type is not TimeType#STOPWATCH`() {
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            time = "".timeText,
            type = TimeType.TIMER
        )
        val expected = mapOf(SegmentField.Time to SegmentFieldError.InvalidSegmentTime)

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should not return error when segment time is less or equal to zero and type is TimeType#STOPWATCH`() {
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            time = "".timeText,
            type = TimeType.STOPWATCH
        )
        val expected = emptyMap<SegmentField, SegmentFieldError>()

        val actual = validator.validate(inputs = inputs)

        assertThat(actual).isEqualTo(expected)
    }
}
