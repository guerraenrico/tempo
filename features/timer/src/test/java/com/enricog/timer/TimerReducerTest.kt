package com.enricog.timer

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.Count
import com.enricog.timer.models.SegmentStep
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


class TimerReducerTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = TimerReducer()

    @Test
    fun `test setup`() = coroutineRule {
        val segment = Segment.EMPTY
        val routine = Routine.EMPTY.copy(
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
    fun `test progressTime should return same state when count is not running`() = coroutineRule {
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
    fun `test progressTime should update state when count is running`() = coroutineRule {
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
    fun `test toggleTimeRunning should return the same state when count is completed`() = coroutineRule {
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
    fun `test toggleTimeRunning should just complete count when isStopwatchRunning`() = coroutineRule {
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
    fun `test toggleTimeRunning should toggle time running`() = coroutineRule {
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
    fun `test restartTime should set count timeInSeconds with the one in the segment`() = coroutineRule {
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
    fun `test nextStep should return the same state when last segment is running`() = coroutineRule {
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
    fun `test nextStep should move to next segment`() = coroutineRule {
        val segment = Segment.EMPTY.copy(timeInSeconds = 10, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(
            startTimeOffsetInSeconds = 5,
            segments = listOf(Segment.EMPTY, segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = false, isCompleted = false),
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
}