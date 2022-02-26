package com.enricog.features.timer.models

import com.enricog.data.routines.testing.EMPTY
import com.enricog.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.entities.seconds
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
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        assertTrue(sut.isCountRunning)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isCountRunning)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isCountRunning)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
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
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertTrue(sut.isCountCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
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
                    Segment.EMPTY.copy(id = 1.asID), Segment.EMPTY.copy(id = 2.asID)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2.asID),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        assertTrue(sut.isRoutineCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
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
        assertFalse(sut.isRoutineCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID), Segment.EMPTY.copy(id = 2.asID)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2.asID),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        assertFalse(sut.isRoutineCompleted)

        sut = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID), Segment.EMPTY.copy(id = 2.asID)
                )
            ),
            runningSegment = Segment.EMPTY.copy(id = 2.asID),
            step = SegmentStep(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        assertFalse(sut.isRoutineCompleted)
    }
}
