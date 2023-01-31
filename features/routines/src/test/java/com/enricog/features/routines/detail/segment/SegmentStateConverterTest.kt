package com.enricog.features.routines.detail.segment

import com.enricog.core.compose.api.classes.emptyImmutableList
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
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
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class SegmentStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val stateConverter = SegmentStateConverter()

    @Test
    fun `should map idle state`() = coroutineRule {
        val state = SegmentState.Idle
        val expected = SegmentViewState.Idle

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = SegmentState.Error(exception)
        val expected = SegmentViewState.Error(exception)

        val actual = stateConverter.convert(state = state)

        assertThat(actual).isEqualTo(expected)
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
                type = TimeTypeEntity.REST
            ),
            action = null
        )
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.from(TimeTypeEntity.REST)
            ),
            errors = immutableMapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyImmutableList(),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
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
                type = TimeTypeEntity.REST
            ),
            action = SaveSegmentError
        )
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.from(TimeTypeEntity.REST)
            ),
            errors = immutableMapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = immutableListOf(),
            message = Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data state without time when selected stopwatch time type`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(
                TimeTypeEntity.TIMER,
                TimeTypeEntity.REST,
                TimeTypeEntity.STOPWATCH
            ),
            inputs = SegmentInputs(
                name = "name".toTextFieldValue(),
                time = "0".timeText,
                type = TimeTypeEntity.STOPWATCH
            ),
            action = null
        )
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "name".toTextFieldValue(),
                time = null,
                type = TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }
}
