package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

internal class TimerStateTest {

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false)
    private val state = TimerState.Counting(
        routine = Routine.EMPTY.copy(
            preparationTime = 10.seconds,
            segments = listOf(
                Segment.EMPTY.copy(id = 1.asID, type = TimeType.TIMER),
                Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
            )
        ),
        runningStep = SegmentStep(
            id = 0,
            count = count,
            type = SegmentStepType.PREPARATION,
            segment = Segment.EMPTY.copy(id = 1.asID)
        ),
        steps = listOf(
            SegmentStep(
                id = 0,
                count = count,
                type = SegmentStepType.PREPARATION,
                segment = Segment.EMPTY.copy(id = 1.asID)
            ),
            SegmentStep(
                id = 1,
                count = count,
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(id = 1.asID)
            ),
            SegmentStep(
                id = 2,
                count = count,
                type = SegmentStepType.PREPARATION,
                segment = Segment.EMPTY.copy(id = 2.asID)
            ),
            SegmentStep(
                id = 3,
                count = count,
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(id = 2.asID)
            )
        ),
        startedAt = OffsetDateTime.now(clock),
        skipCount = 1,
        timerTheme = TimerTheme.DEFAULT,
        timerSettings = TimerSettings.DEFAULT
    )

    @Test
    fun `test when routine is completed`() {
        val inputs = listOf(
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID)
                )
            ) to true,
            state.copy(
                runningStep = SegmentStep(
                    id = 2,
                    count = count.copy(isRunning = true, isCompleted = true),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(id = 2.asID)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 2,
                    count = count.copy(isRunning = true, isCompleted = false),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(id = 2.asID)
                )
            ) to false
        )

        inputs.forEach { (state, expected) ->
            val actual = state.isRoutineCompleted
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `test when stopwatch is running`() {
        val inputs = listOf(
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
                )
            ) to true,
            state.copy(
                runningStep = SegmentStep(
                    id = 1,
                    count = count.copy(isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 1.asID, type = TimeType.TIMER)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
                )
            ) to true,
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = true, isCompleted = false),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
                )
            ) to false,
        )

        inputs.forEach { (state, expected) ->
            val actual = state.isStopwatchRunning
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `test effective total time seconds`() {
        val clock = Clock.fixed(Instant.parse("2023-04-03T10:16:30.00Z"), ZoneId.of("UTC"))
        val expected = 60.seconds

        val actual = state.effectiveTotalSeconds(clock)

        assertThat(actual).isEqualTo(expected)
    }
}
