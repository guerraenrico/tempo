package com.enricog.features.timer

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import org.junit.Test
import kotlin.test.assertEquals

class TimerReducerTest {

    private val sut = TimerReducer()

    @Test
    fun `should setup state with starting segment when start timer offset is more than 0`() {
        val segment = Segment.EMPTY.copy(time = 5.seconds)
        val routine = Routine.EMPTY.copy(
            startTimeOffset = 10.seconds,
            segments = listOf(segment)
        )
        val state = TimerState.Idle
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(seconds = 10.seconds),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )

        val actual = sut.setup(state = state, routine = routine)

        assertEquals(expected, actual)
    }

    @Test
    fun `should setup state with segment in progress when start timer offset is 0`() {
        val segment = Segment.EMPTY.copy(time = 5.seconds)
        val routine = Routine.EMPTY.copy(
            startTimeOffset = 0.seconds,
            segments = listOf(segment)
        )
        val state = TimerState.Idle
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(seconds = 5.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.setup(state = state, routine = routine)

        assertEquals(expected, actual)
    }

    @Test
    fun `should setup state with same sound enable status when current state is counting`() {
        val segment = Segment.EMPTY.copy(time = 5.seconds)
        val routine = Routine.EMPTY.copy(
            startTimeOffset = 10.seconds,
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.start(seconds = 3.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = false
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(seconds = 10.seconds),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = false
        )

        val actual = sut.setup(state = state, routine = routine)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return error state on error`() {
        val exception = Exception()
        val expected = TimerState.Error(throwable = exception)

        val actual = sut.error(throwable = exception)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return same state on progressing time when count is not running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(state, actual)
    }

    @Test
    fun `should update state progressing the time when count is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 9.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state progressing time by counting down when running a starting segment`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 9.seconds),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state by progressing the time base on the segment type defined progress`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 11.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state by completing the step on progressing time when a starting segment is running and count reach zero`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 1.seconds),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state by completing the step on progressing time when a timer segment is running and count reach zero`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 1.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state by completing the step on progressing time when a rest segment is running and count reach zero`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.REST, time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 1.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.REST, time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state by increasing the step count on progressing time when a stopwatch segment is running and count is zero`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 1.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 0.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 1.seconds),
            step = SegmentStep(
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.progressTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return same state on toggling the timer when the count is completed`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.toggleTimeRunning(state)

        assertEquals(state, actual)
    }

    @Test
    fun `should update the state with completed count on toggling the timer when stopwatch segment is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.STOPWATCH)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.toggleTimeRunning(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state with count not running on toggle timer when a not stopwatch segment count is not completed and running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.toggleTimeRunning(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state with reset second count on reset timer when a segment is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.restartTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state with reset second count on reset timer when a segment is starting`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(startTimeOffset = 10.seconds),
            runningSegment = Segment.EMPTY.copy(time = 99.seconds),
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(startTimeOffset = 10.seconds),
            runningSegment = Segment.EMPTY.copy(time = 99.seconds),
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )

        val actual = sut.restartTime(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the same state on next step when last segment is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.nextStep(state)

        assertEquals(state, actual)
    }

    @Test
    fun `should update state with new segment on next step`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            startTimeOffset = 5.seconds,
            segments = listOf(Segment.EMPTY, segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = false, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )

        val actual = sut.nextStep(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should return the same state on next step when routine is completed`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = false, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.nextStep(state)

        assertEquals(state, actual)
    }

    @Test
    fun `should update state with starting segment on next step when segment starting count is completed`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.nextStep(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state with next segment starting on next step when current segment is completed`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID), Segment.EMPTY.copy(id = 2.asID)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1.asID),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID), Segment.EMPTY.copy(id = 2.asID)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2.asID),
            step = SegmentStep(
                count = Count.start(seconds = 5.seconds),
                type = SegmentStepType.STARTING
            ),
            isSoundEnabled = true
        )

        val actual = sut.nextStep(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state with reset segment (skip starting) on next step when current segment is completed and next segment is a rest`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, time = 10.seconds, type = TimeType.TIMER),
                    Segment.EMPTY.copy(id = 2.asID, time = 20.seconds, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(
                id = 1.asID,
                time = 10.seconds,
                type = TimeType.TIMER
            ),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, time = 10.seconds, type = TimeType.TIMER),
                    Segment.EMPTY.copy(id = 2.asID, time = 20.seconds, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(
                id = 2.asID,
                time = 20.seconds,
                type = TimeType.REST
            ),
            step = SegmentStep(
                count = Count.start(seconds = 20.seconds),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )

        val actual = sut.nextStep(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should update state with sound disabled on toggling the sound`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = false
        )

        val actual = sut.toggleSound(state)

        assertEquals(expected, actual)
    }
}
