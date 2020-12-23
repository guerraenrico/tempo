package com.enricog.timer

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.*
import com.enricog.ui_components.resources.blue500
import com.enricog.ui_components.resources.darkBlue500
import com.enricog.ui_components.resources.orange500
import com.enricog.ui_components.resources.purple500
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


class TimerStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = TimerStateConverter()

    @Test
    fun `test map TimerState#Idle`() = coroutineRule{
        val state = TimerState.Idle
        val expected = TimerViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map TimerState#Counting`() = coroutineRule{
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment =  Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerViewState.Counting(
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName =  "segment name",
            clockBackgroundColor = purple500,
            isRoutineCompleted = true
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }


    @Test
    fun `test map backgroundColor and stepTitle SegmentStepType#STARTING - TimeType#REST`() = coroutineRule{
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment =  Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        val expected = TimerViewState.Counting(
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            ),
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName =  "segment name",
            clockBackgroundColor = orange500,
            isRoutineCompleted = false
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map backgroundColor and stepTitle SegmentStepType#IN_PROGRESS - TimeType#REST`() = coroutineRule{
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment =  Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerViewState.Counting(
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName =  "segment name",
            clockBackgroundColor = purple500,
            isRoutineCompleted = true
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map backgroundColor and stepTitle SegmentStepType#IN_PROGRESS - TimeType#TIMER`() = coroutineRule{
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment =  Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerViewState.Counting(
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName =  "segment name",
            clockBackgroundColor = blue500,
            isRoutineCompleted = true
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map backgroundColor and stepTitle SegmentStepType#IN_PROGRESS - TimeType#STOPWATCH`() = coroutineRule{
        val state = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment =  Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH),
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        val expected = TimerViewState.Counting(
            step =  SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName =  "segment name",
            clockBackgroundColor = darkBlue500,
            isRoutineCompleted = true
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }
}