package com.enricog.features.routines.detail.segment

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentState.Data.Action.SaveSegmentError
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.models.SegmentViewState.Data.Message
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SegmentStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = SegmentStateConverter()

    @Test
    fun `should map idle state`() = coroutineRule {
        val state = SegmentState.Idle
        val expected = SegmentViewState.Idle

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = SegmentState.Error(exception)
        val expected = SegmentViewState.Error(exception)

        val result = sut.convert(state = state)

        assertEquals(expected, result)
    }

    @Test
    fun `should map data state without action`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyList(),
            inputs = SegmentInputs(
                name = "name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.REST
            ),
            action = null
        )
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.REST
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyList(),
            message = null
        )

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should map data state with action`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyList(),
            inputs = SegmentInputs(
                name = "name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.REST
            ),
            action = SaveSegmentError
        )
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.REST
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyList(),
            message = Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        )

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }
}
