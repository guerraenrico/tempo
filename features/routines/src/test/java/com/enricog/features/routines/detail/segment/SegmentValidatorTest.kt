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
        val selectedTimeType = TimeType.TIMER
        val inputs = SegmentInputs(
            name = "".toTextFieldValue(),
            rounds = "1".toTextFieldValue(),
            time = "10".timeText
        )
        val expected = mapOf(SegmentField.Name to SegmentFieldError.BlankSegmentName)

        val actual = validator.validate(inputs = inputs, selectedTimeType = selectedTimeType)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when segment rounds is blank`() {
        val selectedTimeType = TimeType.TIMER
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            rounds = "".toTextFieldValue(),
            time = "10".timeText
        )
        val expected = mapOf(SegmentField.Rounds to SegmentFieldError.BlankSegmentRounds)

        val actual = validator.validate(inputs = inputs, selectedTimeType = selectedTimeType)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when segment rounds is zero`() {
        val selectedTimeType = TimeType.TIMER
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            rounds = "0".toTextFieldValue(),
            time = "10".timeText
        )
        val expected = mapOf(SegmentField.Rounds to SegmentFieldError.BlankSegmentRounds)

        val actual = validator.validate(inputs = inputs, selectedTimeType = selectedTimeType)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error when segment time is less or equal to zero and type is not TimeType#STOPWATCH`() {
        val selectedTimeType = TimeType.TIMER
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            rounds = "1".toTextFieldValue(),
            time = "".timeText
        )
        val expected = mapOf(SegmentField.Time to SegmentFieldError.InvalidSegmentTime)

        val actual = validator.validate(inputs = inputs, selectedTimeType = selectedTimeType)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should not return error when segment time is less or equal to zero and segment type stopwatch`() {
        val selectedTimeType = TimeType.STOPWATCH
        val inputs = SegmentInputs(
            name = "name".toTextFieldValue(),
            rounds = "1".toTextFieldValue(),
            time = "".timeText
        )
        val expected = emptyMap<SegmentField, SegmentFieldError>()

        val actual = validator.validate(inputs = inputs, selectedTimeType = selectedTimeType)

        assertThat(actual).isEqualTo(expected)
    }
}
