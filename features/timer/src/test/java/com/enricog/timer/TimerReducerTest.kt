package com.enricog.timer

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.Count
import com.enricog.timer.models.SegmentStep
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import kotlin.test.assertEquals
import org.junit.Test

class TimerReducerTest {

    private val sut = TimerReducer()

    @Test
    fun `test setup should set SegmentStep#type to SegmentStepType#STARTING when Routine#startTimeOffsetInSeconds is more than 0`() {
        val segment = Segment.EMPTY
        val routine = Routine.EMPTY.copy(
            startTimeOffsetInSeconds = 10,
            segments = listOf(segment)
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(timeInSeconds = routine.startTimeOffsetInSeconds),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.setup(routine)

        assertEquals(expected, result)
    }

    @Test
    fun `test setup should set SegmentStep#type to SegmentStepType#IN_PROGRESS when Routine#startTimeOffsetInSeconds is 0`() {
        val segment = Segment.EMPTY
        val routine = Routine.EMPTY.copy(
            startTimeOffsetInSeconds = 0,
            segments = listOf(segment)
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(timeInSeconds = routine.startTimeOffsetInSeconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.setup(routine)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should return same state when count is not running`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(state, result)
    }

    @Test
    fun `test progressTime should update state when count is running`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 9, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should progress by -1 when step#type is STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 0),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 10),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 0),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 9),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should progress by -1 when segmentStepType is STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 0),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 10),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 0),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 9),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should progress by timerType#progress when segmentStepType is not STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 0),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 10),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 0),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 11),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime step#type#STARTING should complete when timeInSeconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, timeInSeconds = 10),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 1),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, timeInSeconds = 10),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#TIMER should complete when timeInSeconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, timeInSeconds = 10),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 1),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, timeInSeconds = 10),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#REST should complete when timeInSeconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.REST, timeInSeconds = 10),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 1),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.REST, timeInSeconds = 10),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#STOPWATCH should not complete when timeInSeconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 1),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 0),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, timeInSeconds = 1),
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test toggleTimeRunning should return the same state when count is completed`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(state, result)
    }

    @Test
    fun `test toggleTimeRunning should just complete count when isStopwatchRunning`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.STOPWATCH)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test toggleTimeRunning should toggle time running`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test toggleTimeRunning should completeCount when is running segment type is STOPWATCH`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH),
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH),
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test restartTime should set count timeInSeconds with the one in the segment when  when SegmentStepType#IN_PROGRESS`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.restartTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test restartTime should set count timeInSeconds with startTimeOffsetInSeconds when SegmentStepType#STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(startTimeOffsetInSeconds = 10),
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 99),
            step = SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(startTimeOffsetInSeconds = 10),
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 99),
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.restartTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should return the same state when last segment is running`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.nextStep(state)

        assertEquals(state, result)
    }

    @Test
    fun `test nextStep should move to next segment`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            startTimeOffsetInSeconds = 5,
            segments = listOf(Segment.EMPTY, segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = false, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should return same state when routine is completed`() {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = false, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.nextStep(state)

        assertEquals(state, result)
    }

    @Test
    fun `test nextStep should set segmentStepType to IN_PROGRESS when segmentStepType is STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 10),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 10),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 10),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should set next segment with segmentStepType STARTING when segmentStepType is IN_PROGRESS`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1), Segment.EMPTY.copy(id = 2)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1), Segment.EMPTY.copy(id = 2)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 5),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should set next segment with segmentStepType IN_PROGRESS when the next segment timeType is REST (skip STARTIING)`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1, timeInSeconds = 10, type = TimeType.TIMER),
                    Segment.EMPTY.copy(id = 2, timeInSeconds = 20, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1, timeInSeconds = 10, type = TimeType.TIMER),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1, timeInSeconds = 10, type = TimeType.TIMER),
                    Segment.EMPTY.copy(id = 2, timeInSeconds = 20, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2, timeInSeconds = 20, type = TimeType.REST),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 20),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }
}
