package com.enricog.timer.models

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimerStateTest {

    @Test
    fun `test isCountRunning`() {
        var sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        assertTrue(sut.isCountRunning)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isCountRunning)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = false, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isCountRunning)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isCountRunning)
    }

    @Test
    fun `test isCountCompleted`() {
        var sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertTrue(sut.isCountCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 10, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isCountCompleted)
    }

    @Test
    fun `test isRoutineCompleted`() {
        var sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 1), Segment.EMPTY.copy(id = 2)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        assertTrue(sut.isRoutineCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
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
        assertFalse(sut.isRoutineCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 1), Segment.EMPTY.copy(id = 2)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isRoutineCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 1), Segment.EMPTY.copy(id = 2)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2),
            step = SegmentStep(
                count = Count(timeInSeconds = 0, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        assertFalse(sut.isRoutineCompleted)
    }

    @Test
    fun `test progressTime should progress by -1 when step#type is STARTING`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should progress by -1 when segmentStepType is STARTING`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime should progress by timerType#progress when segmentStepType is not STARTING`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime step#type#STARTING should complete when timeInSeconds is 0`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#TIMER should complete when timeInSeconds is 0`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#REST should complete when timeInSeconds is 0`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test progressTime runningSegment#type#STOPWATCH should not complete when timeInSeconds is 0`() {
        val sut = TimerState.Counting(
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

        val result = sut.progressTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test toggleTimeRunning`() {
        val sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.toggleTimeRunning()

        assertEquals(expected, result)
    }

    @Test
    fun `test restartTime running segment`() {
        val sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 99),
            step = SegmentStep(
                count = Count(timeInSeconds = 50, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 99),
            step = SegmentStep(
                count = Count(timeInSeconds = 99, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.restartTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test restartTime starting segment`() {
        val sut = TimerState.Counting(
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

        val result = sut.restartTime()

        assertEquals(expected, result)
    }

    @Test
    fun `test completeCount`() {
        val sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 1, isRunning = false, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.completeCount()

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should set segmentStepType to IN_PROGRESS when segmentStepType is STARTING`() {
        val sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(timeInSeconds = 10),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 0),
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

        val result = sut.nextStep()

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should set next segment with segmentStepType STARTING when segmentStepType is IN_PROGRESS`() {
        val sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1), Segment.EMPTY.copy(id = 2)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 0),
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

        val result = sut.nextStep()

        assertEquals(expected, result)
    }

    @Test
    fun `test nextStep should set next segment with segmentStepType IN_PROGRESS when segmentStepType is IN_PROGRESS and new segment timeType is REST`() {
        val sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1, timeInSeconds = 10), Segment.EMPTY.copy(id = 2, timeInSeconds = 20, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 1, timeInSeconds = 10),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 0),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                startTimeOffsetInSeconds = 5,
                segments = listOf(
                    Segment.EMPTY.copy(id = 1, timeInSeconds = 10), Segment.EMPTY.copy(id = 2, timeInSeconds = 20, type = TimeType.REST)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2, timeInSeconds = 20, type = TimeType.REST),
            step = SegmentStep(
                count = Count.start(timeInSeconds = 20),
                type = SegmentStepType.IN_PROGRESS
            )
        )

        val result = sut.nextStep()

        assertEquals(expected, result)
    }

}