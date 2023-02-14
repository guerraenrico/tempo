package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class TimerStateTest {

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
            type = SegmentStepType.STARTING,
            segment = Segment.EMPTY.copy(id = 1.asID)
        ),
        steps = listOf(
            SegmentStep(
                id = 0,
                count = count,
                type = SegmentStepType.STARTING,
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
                type = SegmentStepType.STARTING,
                segment = Segment.EMPTY.copy(id = 2.asID)
            ),
            SegmentStep(
                id = 3,
                count = count,
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(id = 2.asID)
            )
        ),
        isSoundEnabled = true
    )

    @Test
    fun `test when step count is running`() {
        val arguments = listOf(
            count.copy(isRunning = true, isCompleted = false) to true,
            count.copy(isRunning = true, isCompleted = true) to false,
            count.copy(isRunning = false, isCompleted = true) to false,
            count.copy(isRunning = false, isCompleted = false) to false
        )
        val inputs = arguments.map { (count, expected) ->
            state.copy(runningStep = state.runningStep.copy(count = count)) to expected
        }

        inputs.forEach { (state, expected) ->
            val actual = state.isStepCountRunning
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `test when step count is completed`() {
        val arguments = listOf(
            count.copy(isRunning = true, isCompleted = false) to false,
            count.copy(isRunning = true, isCompleted = true) to true
        )
        val inputs = arguments.map { (count, expected) ->
            state.copy(runningStep = state.runningStep.copy(count = count)) to expected
        }

        inputs.forEach { (state, expected) ->
            val actual = state.isStepCountCompleted
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun `test when step count is completing`() {
        val inputs = listOf(
            state.copy(
                runningStep = SegmentStep(
                    id = 1,
                    count = count.copy(seconds = 10.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.TIMER)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 1,
                    count = count.copy(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.TIMER)
                )
            ) to true,
            state.copy(
                runningStep = SegmentStep(
                    id = 1,
                    count = count.copy(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 1,
                    count = count.copy(seconds = 5.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.TIMER)
                )
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 1,
                    count = count.copy(seconds = 0.seconds, isRunning = true, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.TIMER)
                )
            ) to false,
        )

        inputs.forEach { (state, expected) ->
            val actual = state.isStepCountCompleting
            assertThat(actual).isEqualTo(expected)
        }
    }

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
                    type = SegmentStepType.STARTING,
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
                    type = SegmentStepType.STARTING,
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
            ) to false,
            state.copy(
                runningStep = SegmentStep(
                    id = 3,
                    count = count.copy(isRunning = true, isCompleted = false),
                    type = SegmentStepType.STARTING,
                    segment = Segment.EMPTY.copy(id = 2.asID, type = TimeType.STOPWATCH)
                )
            ) to false,
        )

        inputs.forEach { (state, expected) ->
            val actual = state.isStopwatchRunning
            assertThat(actual).isEqualTo(expected)
        }
    }
}
