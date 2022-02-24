package com.enricog.features.timer

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import kotlin.test.assertEquals
import org.junit.Test

class TimerReducerTest {

    private val sut = TimerReducer()

    @Test
    fun `test setup should set SegmentStep#type to SegmentStepType#STARTING when Routine#startTimeOffsetInSeconds is more than 0`() {
        val segment = Segment.EMPTY
        val routine = Routine.EMPTY.copy(
            startTimeOffset = 10.seconds,
            segments = listOf(segment)
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(seconds = routine.startTimeOffset),
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
            startTimeOffset = 0.seconds,
            segments = listOf(segment)
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count.idle(seconds = routine.startTimeOffset),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.setup(routine)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should return same state when count is not running`() {
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
            )
        )

        val result = sut.progressTime(state)

        assertEquals(state, result)
    }

    @Test
    fun `test progressTime should update state when count is running`() {
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
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 9.seconds, isRunning = true, isCompleted = false),
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
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 9.seconds),
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
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 9.seconds),
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
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 11.seconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime step#type#STARTING should complete when seconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 1.seconds),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#TIMER should complete when seconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 1.seconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#REST should complete when seconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.REST, time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 1.seconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.REST, time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#STOPWATCH should not complete when seconds is 0`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 1.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 0.seconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 1.seconds),
            step = SegmentStep(
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.progressTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test toggleTimeRunning should return the same state when count is completed`() {
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
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(state, result)
    }

    @Test
    fun `test toggleTimeRunning should just complete count when isStopwatchRunning`() {
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
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test toggleTimeRunning should toggle time running`() {
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
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
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
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(type = TimeType.STOPWATCH),
            step = SegmentStep(
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test restartTime should set count seconds with the one in the segment when  when SegmentStepType#IN_PROGRESS`() {
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
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.restartTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test restartTime should set count seconds with startTimeOffsetInSeconds when SegmentStepType#STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(startTimeOffset = 10.seconds),
            runningSegment = Segment.EMPTY.copy(time = 99.seconds),
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(startTimeOffset = 10.seconds),
            runningSegment = Segment.EMPTY.copy(time = 99.seconds),
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.restartTime(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should return the same state when last segment is running`() {
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
            )
        )

        val result = sut.nextStep(state)

        assertEquals(state, result)
    }

    @Test
    fun `test nextStep should move to next segment`() {
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
            )
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningSegment = segment,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should return same state when routine is completed`() {
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
            )
        )

        val result = sut.nextStep(state)

        assertEquals(state, result)
    }

    @Test
    fun `test nextStep should set segmentStepType to IN_PROGRESS when segmentStepType is STARTING`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(time = 10.seconds),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(time = 10.seconds),
            step = SegmentStep(
                count = Count.start(seconds = 10.seconds),
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
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID), Segment.EMPTY.copy(id = 2.asID)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1.asID),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
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
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should set next segment with segmentStepType IN_PROGRESS when the next segment timeType is REST (skip STARTIING)`() {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, time = 10.seconds, type = TimeType.TIMER),
                    Segment.EMPTY.copy(id = 2.asID, time = 20.seconds, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1.asID, time = 10.seconds, type = TimeType.TIMER),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffset = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, time = 10.seconds, type = TimeType.TIMER),
                    Segment.EMPTY.copy(id = 2.asID, time = 20.seconds, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2.asID, time = 20.seconds, type = TimeType.REST),
            step = SegmentStep(
                count = Count.start(seconds = 20.seconds),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.nextStep(state)

        assertEquals(expected, result)
    }
}
