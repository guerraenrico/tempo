package com.enricog.timer.models

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

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
}
