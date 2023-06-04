package com.enricog.features.routines.detail.segment

import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentState.Data.Action.SaveSegmentError
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SegmentReducerTest {

    private val reducer = SegmentReducer()

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
        val timerTheme = TimerTheme.DEFAULT
        val expected = SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = timerTheme
        )

        val actual = reducer.setup(routine = routine, segmentId = 1.asID, timerTheme = timerTheme)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should setup state with new segment`() {
        val segment = Segment.create(Rank.from("mzzzzz"))
        val routine = Routine.EMPTY.copy(
            segments = emptyList()
        )
        val timerTheme = TimerTheme.DEFAULT
        val expected = SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = timerTheme
        )

        val actual = reducer.setup(routine = routine, segmentId = 0.asID, timerTheme = timerTheme)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should setup error state`() {
        val exception = Exception()
        val expected = SegmentState.Error(throwable = exception)

        val actual = reducer.error(throwable = exception)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should remove field name error when segment name is updated`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(name = "Segment Name"),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(name = "Segment Name"),
            errors = mapOf(
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.updateSegmentName(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should remove field time error when segment time is updated`() {
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
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
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
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.updateSegmentTime(state = state)

        assertThat(actual).isEqualTo(expected)
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
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.updateSegmentTimeType(state = expected, timeType = TimeType.TIMER)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should clear time error when segment type is selected`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY.copy(
                segments = emptyList()
            ),
            segment = Segment.EMPTY.copy(
                type = TimeType.TIMER,
                time = 10.seconds
            ),
            errors = mapOf(
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
                SegmentField.Time to SegmentFieldError.InvalidSegmentTime,
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
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
                SegmentField.Name to SegmentFieldError.BlankSegmentName,
            ),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.REST,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.updateSegmentTimeType(state = state, timeType = TimeType.REST)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply segment errors`() {
        val errors = mapOf(
            SegmentField.Name to SegmentFieldError.BlankSegmentName,
            SegmentField.Time to SegmentFieldError.InvalidSegmentTime
        )
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = errors,
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.applySegmentErrors(state = state, errors = errors)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply segment error action when save throws an error`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = SaveSegmentError,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.saveSegmentError(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should reset action when it is handled`() {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = SaveSegmentError,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH),
            selectedTimeType = TimeType.TIMER,
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )

        val actual = reducer.actionHandled(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}
