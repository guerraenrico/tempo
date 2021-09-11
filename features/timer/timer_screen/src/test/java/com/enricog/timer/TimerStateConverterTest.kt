package com.enricog.timer

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.timer_service.service.Count
import com.enricog.timer_service.service.SegmentStep
import com.enricog.timer_service.service.SegmentStepType
import com.enricog.timer_service.models.TimerState
import com.enricog.timer_service.service.TimerViewState
import com.enricog.ui_components.resources.TimeTypeColors
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class TimerStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = TimerStateConverter()

    @Test
    fun `test map TimerState#Idle`() = coroutineRule {
        val state = TimerState.Idle
        val expected = com.enricog.timer_service.service.TimerViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map TimerState#Counting`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            )
        )
        val expected = com.enricog.timer_service.service.TimerViewState.Counting(
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
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
    fun `test map backgroundColor and stepTitle SegmentStepType#STARTING - TimeType#REST`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.STARTING
            )
        )
        val expected = com.enricog.timer_service.service.TimerViewState.Counting(
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.STARTING
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
    fun `test map backgroundColor and stepTitle SegmentStepType#IN_PROGRESS - TimeType#REST`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            )
        )
        val expected = com.enricog.timer_service.service.TimerViewState.Counting(
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
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
    fun `test map backgroundColor and stepTitle SegmentStepType#IN_PROGRESS - TimeType#TIMER`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            )
        )
        val expected = com.enricog.timer_service.service.TimerViewState.Counting(
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
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
    fun `test map backgroundColor and stepTitle SegmentStepType#IN_PROGRESS - TimeType#STOPWATCH`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH),
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
            )
        )
        val expected = com.enricog.timer_service.service.TimerViewState.Counting(
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 5.seconds,
                    isRunning = true,
                    isCompleted = true
                ),
                type = com.enricog.timer_service.service.SegmentStepType.IN_PROGRESS
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
