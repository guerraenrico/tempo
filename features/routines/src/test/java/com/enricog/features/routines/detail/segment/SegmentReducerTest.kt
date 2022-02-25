package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.testing.EMPTY
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentState
import org.junit.Test
import kotlin.test.assertEquals

class SegmentReducerTest {

    private val sut = SegmentReducer()

    @Test
    fun `should setup state with existing segment`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val expected = SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.setup(routine = routine, segmentId = 1.asID)

        assertEquals(expected, result)
    }

    @Test
    fun `should setup state with new segment`() {
        val segment = Segment.create(Rank.from("mzzzzz"))
        val routine = Routine.EMPTY.copy(
            segments = emptyList()
        )
        val expected = SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.setup(routine = routine, segmentId = 0.asID)

        assertEquals(expected, result)
    }

    @Test
    fun `should update segment name and remove field name error`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(name = ""),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(name = "name"),
            errors = mapOf(
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.updateSegmentName(state = state, text = "name")

        assertEquals(expected, result)
    }

    @Test
    fun `should update segment time and remove field time error`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 0.seconds,
                type = TimeType.TIMER
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.TIMER
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.updateSegmentTime(state = state, seconds = 10.seconds)

        assertEquals(expected, result)
    }

    @Test
    fun `should set segment time to zero when segment#type is TimeType#STOPWATCH`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.STOPWATCH
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 0.seconds,
                type = TimeType.STOPWATCH
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.updateSegmentTime(state = state, seconds = 10.seconds)

        assertEquals(expected, result)
    }

    @Test
    fun `should update segment type and keep segment time when selected type is not TimeType#STOPWATCH`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                type = TimeType.TIMER,
                time = 10.seconds
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.REST
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.updateSegmentTimeType(state = state, timeType = TimeType.REST)

        assertEquals(expected, result)
    }

    @Test
    fun `should update segment type and set segment time to zero when selected type not TimeType#STOPWATCH`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.TIMER
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 0.seconds,
                type = TimeType.STOPWATCH
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.updateSegmentTimeType(state = state, timeType = TimeType.STOPWATCH)

        assertEquals(expected, result)
    }

    @Test
    fun `should apply segment errors`() {
        val errors = mapOf(
            SegmentField.Name to SegmentFieldError.BlankSegmentName,
            SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
        )
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = errors,
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val result = sut.applySegmentErrors(state = state, errors = errors)

        assertEquals(expected, result)
    }
}
