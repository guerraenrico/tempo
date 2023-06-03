package com.enricog.features.timer

import androidx.compose.ui.graphics.Color
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.Background
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class TimerStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val stateConverter = TimerStateConverter(clock = clock)

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
            routine = Routine.EMPTY.copy(
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
            startedAt = OffsetDateTime.now(clock),
            skipCount = 0,
            timerTheme = TimerTheme.DEFAULT,
            timerSettings = TimerSettings.DEFAULT
        )
        val expected = TimerViewState.Counting(
            timeInSeconds = 5,
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackground = Background(
                background = Color.Green,
                ripple = null
            ),
            clockOnBackgroundColor = Color.White,
            timerActions = TimerViewState.Counting.Actions(
                back = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_back,
                    contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_stop,
                    contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
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
            routine = Routine.EMPTY.copy(
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
            startedAt = OffsetDateTime.now(clock),
            skipCount = 0,
            timerTheme = TimerTheme.DEFAULT,
            timerSettings = TimerSettings.DEFAULT
        )
        val expected = TimerViewState.Counting(
            timeInSeconds = 5,
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackground = Background(
                background = Color.Green,
                ripple = null
            ),
            clockOnBackgroundColor = Color.White,
            timerActions = TimerViewState.Counting.Actions(
                back = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_back,
                    contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                    size = TempoIconButtonSize.Normal
                ),
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment,
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
                routine = Routine.EMPTY.copy(
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
                startedAt = OffsetDateTime.now(clock),
                skipCount = 0,
                timerTheme = TimerTheme.DEFAULT,
                timerSettings = TimerSettings.DEFAULT
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_time_type_rest,
                segmentName = "segment name",
                clockBackground = Background(
                    background = Color.Green,
                    ripple = null
                ),
                clockOnBackgroundColor = Color.White,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
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
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.TIMER),
                    )
                ),
                startedAt = OffsetDateTime.now(clock),
                skipCount = 0,
                timerTheme = TimerTheme.DEFAULT,
                timerSettings = TimerSettings.DEFAULT
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackground = Background(
                    background = Color.Red,
                    ripple = null
                ),
                clockOnBackgroundColor = Color.White,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
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
                routine = Routine.EMPTY.copy(
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
                startedAt = OffsetDateTime.now(clock),
                skipCount = 0,
                timerTheme = TimerTheme.DEFAULT,
                timerSettings = TimerSettings.DEFAULT
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackground = Background(
                    background = Color.Black,
                    ripple = null
                ),
                clockOnBackgroundColor = Color.White,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
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
    fun `should map state with -preparation- background and -stopwatch- ripple color when stopwatch segment in preparation step is completed`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY.copy(
                    preparationTime = 5.seconds,
                    segments = listOf(
                        Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                    )
                ),
                runningStep = SegmentStep(
                    id = 0,
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                ),
                steps = listOf(
                    SegmentStep(
                        id = 0,
                        count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                        type = SegmentStepType.PREPARATION,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                    ),
                    SegmentStep(
                        id = 1,
                        count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name", type = TimeType.STOPWATCH)
                    )
                ),
                startedAt = OffsetDateTime.now(clock),
                skipCount = 0,
                timerTheme = TimerTheme.DEFAULT,
                timerSettings = TimerSettings.DEFAULT
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_preparation,
                segmentName = "segment name",
                clockBackground = Background(
                    background = Color.Blue,
                    ripple = Color.Black
                ),
                clockOnBackgroundColor = Color.White,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
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
    fun `should map state with -stopwatch- background and -preparation- ripple color when stopwatch segment is completed and next step is preparation`() =
        coroutineRule {
            val state = TimerState.Counting(
                routine = Routine.EMPTY.copy(
                    preparationTime = 5.seconds,
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
                        type = SegmentStepType.PREPARATION,
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
                        type = SegmentStepType.PREPARATION,
                        segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                    ),
                    SegmentStep(
                        id = 3,
                        count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                    )
                ),
                startedAt = OffsetDateTime.now(clock),
                skipCount = 0,
                timerTheme = TimerTheme.DEFAULT,
                timerSettings = TimerSettings.DEFAULT
            )
            val expected = TimerViewState.Counting(
                timeInSeconds = 5,
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name stopwatch",
                clockBackground = Background(
                    background = Color.Black,
                    ripple = Color.Blue
                ),
                clockOnBackgroundColor = Color.White,
                timerActions = TimerViewState.Counting.Actions(
                    back = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_back,
                        contentDescriptionResId = R.string.content_description_button_back_routine_segment,
                        size = TempoIconButtonSize.Normal
                    ),
                    play = TimerViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
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
    fun `should map state to completed when routine is completed`() = coroutineRule {
        val state = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                preparationTime = 5.seconds,
                segments = listOf(
                    Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                )
            ),
            runningStep = SegmentStep(
                id = 2,
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.STOPWATCH)
            ),
            steps = listOf(
                SegmentStep(
                    id = 1,
                    count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                ),
                SegmentStep(
                    id = 2,
                    count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment name timer", type = TimeType.TIMER)
                )
            ),
            startedAt = OffsetDateTime.now(clock),
            skipCount = 1,
            timerTheme = TimerTheme.DEFAULT,
            timerSettings = TimerSettings.DEFAULT
        )
        val expected = TimerViewState.Completed(
            effectiveTotalTime = "0".timeText,
            skipCount = 1
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }
}
