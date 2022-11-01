package com.enricog.features.timer

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.theme.TimeTypeColors
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class TimerStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = TimerStateConverter()

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = TimerState.Idle
        val expected = TimerViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = TimerState.Error(throwable = exception)
        val expected = TimerViewState.Error(throwable = exception)

        val result = sut.convert(state)

        assertEquals(expected, result)
    }


    @Test
    fun `test map counting state`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackgroundColor = TimeTypeColors.REST,
            isRoutineCompleted = true,
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map backgroundColor and stepTitle when rest segment is starting`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.STARTING
                )
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.STARTING
                ),
                stepTitleId = R.string.title_segment_time_type_rest,
                segmentName = "segment name",
                clockBackgroundColor = TimeTypeColors.STARTING,
                isRoutineCompleted = false,
            )

            val result = sut.convert(state)

            assertEquals(expected, result)
        }

    @Test
    fun `test map backgroundColor and stepTitle when rest segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_time_type_rest,
                segmentName = "segment name",
                clockBackgroundColor = TimeTypeColors.REST,
                isRoutineCompleted = true,
            )

            val result = sut.convert(state)

            assertEquals(expected, result)
        }

    @Test
    fun `test map backgroundColor and stepTitle when timer segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackgroundColor = TimeTypeColors.TIMER,
                isRoutineCompleted = true,
            )

            val result = sut.convert(state)

            assertEquals(expected, result)
        }

    @Test
    fun `test map backgroundColor and stepTitle when stopwatch segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(
                    name = "segment name",
                    type = TimeType.STOPWATCH
                ),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackgroundColor = TimeTypeColors.STOPWATCH,
                isRoutineCompleted = true,
            )

            val result = sut.convert(state)

            assertEquals(expected, result)
        }
}
