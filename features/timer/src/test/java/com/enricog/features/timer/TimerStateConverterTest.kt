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
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.theme.TimeTypeColors
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class TimerStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val stateConverter = TimerStateConverter()

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = TimerState.Idle
        val expected = TimerViewState.Idle

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = TimerState.Error(throwable = exception)
        val expected = TimerViewState.Error(throwable = exception)

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }


    @Test
    fun `test map counting state with timer running`() = coroutineRule {
        val state = TimerState.Counting(
            routine =  Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.REST))
            ),
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST)
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST)
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerViewState.Counting(
            timeInSeconds = 5,
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackgroundColor = BackgroundColor(
                background =  TimeTypeColors.REST,
                ripple = null
            ),
            isSoundEnabled = true,
            timerActions = TimerViewState.Counting.Actions(
                back = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_back,
                    contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                play = TimerViewState.Counting.Actions.Button(
                    iconResId =  R.drawable.ic_timer_stop,
                    contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                next = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_next,
                    contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map counting state with timer not running`() = coroutineRule {
        val state = TimerState.Counting(
            routine =  Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.REST))
            ),
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST)
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST)
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerViewState.Counting(
            timeInSeconds = 5,
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackgroundColor = BackgroundColor(
                background =  TimeTypeColors.REST,
                ripple = null
            ),
            isSoundEnabled = true,
            timerActions = TimerViewState.Counting.Actions(
                back = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_back,
                    contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                play = TimerViewState.Counting.Actions.Button(
                    iconResId =  R.drawable.ic_timer_play,
                    contentDescriptionResId =  R.string.content_description_button_start_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                next = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_next,
                    contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map state with -rest- background color and title when rest segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine =  Routine.EMPTY.copy(
                    segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.REST))
                ),
                runningStep = SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST)
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST)
                    )
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_time_type_rest,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    background =  TimeTypeColors.REST,
                    ripple = null
                ),
                isSoundEnabled = true,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId =  R.drawable.ic_timer_stop,
                        contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    next = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_next,
                        contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                        size = TempoIconButtonSize.Normal
                    )
                )
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `should map state with -timer- background color and -in progress- title when timer segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY.copy(
                    segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER))
                ),
                runningStep = SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment =Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment =Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
                    )
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    background =  TimeTypeColors.TIMER,
                    ripple = null
                ),
                isSoundEnabled = true,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId =  R.drawable.ic_timer_stop,
                        contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    next = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_next,
                        contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                        size = TempoIconButtonSize.Normal
                    )
                )
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `should map state with -stopwatch- background color and -in progress- title when stopwatch segment is in progress`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine =  Routine.EMPTY.copy(
                    segments = listOf(Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH))
                ),
                runningStep = SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH),
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 0.seconds, isRunning = true, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH),
                    )
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    background =  TimeTypeColors.STOPWATCH,
                    ripple = null
                ),
                isSoundEnabled = true,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId =  R.drawable.ic_timer_stop,
                        contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                        size = TempoIconButtonSize.Large
                    ),
                    next = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_next,
                        contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                        size = TempoIconButtonSize.Normal
                    )
                )
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
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
                runningStep = SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.STARTING,
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                        type = SegmentStepType.STARTING,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                    ),
                    SegmentStep(
                        id = 1,
                        count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                    )
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_starting,
                segmentName = "segment name",
                clockBackgroundColor = BackgroundColor(
                    background =  TimeTypeColors.STARTING,
                    ripple = TimeTypeColors.STOPWATCH
                ),
                isSoundEnabled = true,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId =  R.drawable.ic_timer_stop,
                        contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                        size = TempoIconButtonSize.Large
                    ),
                    next = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_next,
                        contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                        size = TempoIconButtonSize.Normal
                    )
                )
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
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
                runningStep = SegmentStep(
                    id = 1,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH)
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.STARTING,
                        segment = Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH)
                    ),
                    SegmentStep(
                        id = 1,
                        count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH)
                    ),
                    SegmentStep(
                        id = 2,
                        count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.STARTING,
                        segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                    ),
                    SegmentStep(
                        id = 3,
                        count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                    )
                ),
                isSoundEnabled = true
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name stopwatch",
                clockBackgroundColor = BackgroundColor(
                    background =  TimeTypeColors.STOPWATCH,
                    ripple = TimeTypeColors.STARTING
                ),
                isSoundEnabled = true,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId =  R.drawable.ic_timer_stop,
                        contentDescriptionResId =  R.string.content_description_button_stop_routine_segment,
                        size = TempoIconButtonSize.Large
                    ),
                    next = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_next,
                        contentDescriptionResId = R.string.content_description_button_next_routine_segment,
                        size = TempoIconButtonSize.Normal
                    )
                )
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
        }
}
