package com.enricog.features.routines.detail.segment

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.ui.components.textField.timeText
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SegmentStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = SegmentStateConverter()

    @Test
    fun `should map SegmentState#Idle`() = coroutineRule {
        val state = SegmentState.Idle
        val expected = SegmentViewState.Idle

        val actual = sut.convert(state)

        assertEquals(expected, actual)
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
            timeTypes = emptyList(),
            inputs = SegmentInputs(
                name = "name",
                time = "10".timeText,
                type = TimeType.REST
            )
        )
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "name",
                time = "10".timeText,
                type = TimeType.REST
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = emptyList()
        )

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }
}
