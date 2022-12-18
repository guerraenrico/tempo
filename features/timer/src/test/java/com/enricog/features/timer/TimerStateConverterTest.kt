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
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
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

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = TimerState.Error(throwable = exception)
        val expected = TimerViewState.Error(throwable = exception)

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }


    @Test
    fun `test map counting state`() = coroutineRule {
        val state = TimerState.Counting(
            routine =  Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.REST))
            ),
            runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            isSoundEnabled = true
        )
        val expected = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackgroundColor = BackgroundColor(
                foreground =  TimeTypeColors.REST,
                ripple = null
            ),
            isSoundEnabled = true
        )

        val actual = sut.convert(state)

        assertEquals(expected, actual)
    }

    @Test
    fun `should map state with -rest- background color and title when rest segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine =  Routine.EMPTY.copy(
                    segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.REST))
                ),
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_time_type_rest,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    foreground =  TimeTypeColors.REST,
                    ripple = null
                ),
                isSoundEnabled = true
            )

            val actual = sut.convert(state)

            assertEquals(expected, actual)
        }

    @Test
    fun `should map state with -timer- background color and -in progress- title when timer segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY.copy(
                    segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER))
                ),
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    foreground =  TimeTypeColors.TIMER,
                    ripple = null
                ),
                isSoundEnabled = true
            )

            val actual = sut.convert(state)

            assertEquals(expected, actual)
        }

    @Test
    fun `should map state with -stopwatch- background color and -in progress- title when stopwatch segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine =  Routine.EMPTY.copy(
                    segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH))
                ),
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    foreground =  TimeTypeColors.STOPWATCH,
                    ripple = null
                ),
                isSoundEnabled = true
            )

            val actual = sut.convert(state)

            assertEquals(expected, actual)
        }

    @Test
    fun `should map state with -starting- background and -stopwatch- ripple color when stopwatch segment in starting step is completed`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine =  Routine.EMPTY.copy(
                    startTimeOffset = 5.seconds,
                    segments = listOf(
                        Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                    )
                ),
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.STARTING
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.STARTING
                ),
                stepTitleId = R.string.title_segment_step_type_starting,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    foreground =  TimeTypeColors.STARTING,
                    ripple = TimeTypeColors.STOPWATCH
                ),
                isSoundEnabled = true
            )

            val actual = sut.convert(state)

            assertEquals(expected, actual)
        }

    @Test
    fun `should map state with -stopwatch- background and -starting- ripple color when stopwatch segment is completed and next step is starting`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine =  Routine.EMPTY.copy(
                    startTimeOffset = 5.seconds,
                    segments = listOf(
                        Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH),
                        Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                    )
                ),
                runningSegment = Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                ),
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name stopwatch",
                clockBackgroundColor = BackgroundColor(
                    foreground =  TimeTypeColors.STOPWATCH,
                    ripple = TimeTypeColors.STARTING
                ),
                isSoundEnabled = true
            )

            val actual = sut.convert(state)

            assertEquals(expected, actual)
        }
}
