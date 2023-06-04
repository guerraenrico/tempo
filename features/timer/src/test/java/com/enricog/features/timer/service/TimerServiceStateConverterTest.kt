package com.enricog.features.timer.service

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
import com.enricog.features.timer.R
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

internal class TimerServiceStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val stateConverter = TimerServiceStateConverter()

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = TimerState.Idle
        val expected = TimerServiceViewState.Idle

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map error state`() = coroutineRule {
        val exception = Exception()
        val state = TimerState.Error(throwable = exception)
        val expected = TimerServiceViewState.Error(throwable = exception)

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
        val expected = TimerServiceViewState.Counting(
            time = "00:05",
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackground = TimerServiceViewState.Counting.Background(
                background = "18374966855136706560".toULong().toLong()
            ),
            clockOnBackgroundColor = "18446744069414584320".toULong().toLong(),
            timerActions = TimerServiceViewState.Counting.Actions(
                play = TimerServiceViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_stop,
                    contentDescriptionResId = R.string.content_description_button_stop_routine_segment
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
        val expected = TimerServiceViewState.Counting(
            time = "00:05",
            stepTitleId = R.string.title_segment_time_type_rest,
            segmentName = "segment name",
            clockBackground = TimerServiceViewState.Counting.Background(
                background = "18374966855136706560".toULong().toLong()
            ),
            clockOnBackgroundColor = "18446744069414584320".toULong().toLong(),
            timerActions = TimerServiceViewState.Counting.Actions(
                play = TimerServiceViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment
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
            val expected = TimerServiceViewState.Counting(
                time = "00:05",
                stepTitleId = R.string.title_segment_time_type_rest,
                segmentName = "segment name",
                clockBackground = TimerServiceViewState.Counting.Background(
                    background = "18374966855136706560".toULong().toLong()
                ),
                clockOnBackgroundColor = "18446744069414584320".toULong().toLong(),
                timerActions = TimerServiceViewState.Counting.Actions(
                    play = TimerServiceViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment
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
            val expected = TimerServiceViewState.Counting(
                time = "00:05",
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackground = TimerServiceViewState.Counting.Background(
                    background = "18446462598732840960".toULong().toLong()
                ),
                clockOnBackgroundColor = "18446744069414584320".toULong().toLong(),
                timerActions = TimerServiceViewState.Counting.Actions(
                    play = TimerServiceViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment
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
            val expected = TimerServiceViewState.Counting(
                time = "00:05",
                stepTitleId = R.string.title_segment_step_type_in_progress,
                segmentName = "segment name",
                clockBackground = TimerServiceViewState.Counting.Background(
                    background = "18374686479671623680".toULong().toLong()
                ),
                clockOnBackgroundColor = "18446744069414584320".toULong().toLong(),
                timerActions = TimerServiceViewState.Counting.Actions(
                    play = TimerServiceViewState.Counting.Actions.Button(
                        iconResId = R.drawable.ic_timer_stop,
                        contentDescriptionResId = R.string.content_description_button_stop_routine_segment
                    )
                )
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
        }
}