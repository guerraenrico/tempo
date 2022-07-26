package com.enricog.features.routines.detail.segment

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Test
import kotlin.test.assertEquals

class SegmentReducerTest {

    private val sut = SegmentReducer()

    @Test
    fun `should setup state with existing segment`() {
        val segment = Segment.EMPTY.copy(
            id = 1.asID,
            name = "Segment Name",
            time = 50.seconds,
            type = TimeType.TIMER
        )
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val expected = SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "50".timeText,
                type = TimeType.TIMER
            )
        )

        val actual = sut.setup(routine = routine, segmentId = 1.asID)

        assertEquals(expected, actual)
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
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.TIMER
            )
        )

        val actual = sut.setup(routine = routine, segmentId = 0.asID)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update segment name and remove field name error`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(name = "Segment Name"),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "50".timeText,
                type = TimeType.TIMER
            )
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(name = "Segment Name"),
            errors = mapOf(
                SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name Modified".toTextFieldValue(),
                time = "50".timeText,
                type = TimeType.TIMER
            )
        )

        val actual = sut.updateSegmentName(
            state = state,
            textFieldValue = "Segment Name Modified".toTextFieldValue()
        )

        assertEquals(expected, actual)
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
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.TIMER
            )
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 0.seconds,
                type = TimeType.TIMER
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.TIMER
            )
        )

        val actual = sut.updateSegmentTime(state = state, text = "10".timeText)

        assertEquals(expected, actual)
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
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.STOPWATCH
            )
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.STOPWATCH
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.STOPWATCH
            )
        )

        val actual = sut.updateSegmentTime(state = state, text = "10".timeText)

        assertEquals(expected, actual)
    }

    @Test
    fun `should not update segment type when same segment type is selected`() {
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                type = TimeType.TIMER,
                time = 10.seconds
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.TIMER
            )
        )

        val actual = sut.updateSegmentTimeType(state = expected, timeType = TimeType.TIMER)

        assertEquals(expected, actual)
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
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.TIMER
            )
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.TIMER
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.REST
            )
        )

        val actual = sut.updateSegmentTimeType(state = state, timeType = TimeType.REST)

        assertEquals(expected, actual)
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
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.TIMER
            )
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                time = 10.seconds,
                type = TimeType.TIMER
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.STOPWATCH
            )
        )

        val actual = sut.updateSegmentTimeType(state = state, timeType = TimeType.STOPWATCH)

        assertEquals(expected, actual)
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
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.TIMER
            )
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = errors,
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            inputs = SegmentInputs(
                name = "".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.TIMER
            )
        )

        val actual = sut.applySegmentErrors(state = state, errors = errors)

        assertEquals(expected, actual)
    }
}
