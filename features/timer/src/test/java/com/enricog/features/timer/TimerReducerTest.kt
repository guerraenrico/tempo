package com.enricog.features.timer

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TimerReducerTest {

    private val timerReducer = TimerReducer()

    @Test
    fun `should setup state with starting segment when preparation is more than 0`() {
        val routine = Routine.EMPTY.copy(
            preparationTime = 5.seconds,
            segments = listOf(
                Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH),
                Segment.EMPTY.copy(
                    name = "segment name rest",
                    type = TimeType.REST,
                    time = 4.seconds
                ),
                Segment.EMPTY.copy(
                    name = "segment name timer",
                    type = TimeType.TIMER,
                    time = 10.seconds
                )
            )
        )
        val state = TimerState.Idle
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING,
                segment = Segment.EMPTY.copy(
                    name = "segment name stopwatch",
                    type = TimeType.STOPWATCH
                )
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = Segment.EMPTY.copy(
                        name = "segment name stopwatch",
                        type = TimeType.STOPWATCH
                    )
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 0.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(
                        name = "segment name stopwatch",
                        type = TimeType.STOPWATCH
                    )
                ),
                SegmentStep(
                    id = 2,
                    count = Count.idle(seconds = 4.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(
                        name = "segment name rest",
                        type = TimeType.REST,
                        time = 4.seconds
                    )
                ),
                SegmentStep(
                    id = 3,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = Segment.EMPTY.copy(
                        name = "segment name timer",
                        type = TimeType.TIMER,
                        time = 10.seconds
                    )
                ),
                SegmentStep(
                    id = 4,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(
                        name = "segment name timer",
                        type = TimeType.TIMER,
                        time = 10.seconds
                    )
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.setup(state = state, routine = routine)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should setup state with segment in progress when preparation time is 0`() {
        val routine = Routine.EMPTY.copy(
            preparationTime = 0.seconds,
            segments = listOf(
                Segment.EMPTY.copy(name = "segment name stopwatch", type = TimeType.STOPWATCH),
                Segment.EMPTY.copy(
                    name = "segment name rest",
                    type = TimeType.REST,
                    time = 4.seconds
                ),
                Segment.EMPTY.copy(
                    name = "segment name timer",
                    type = TimeType.TIMER,
                    time = 10.seconds
                )
            )
        )
        val state = TimerState.Idle
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(
                    name = "segment name stopwatch",
                    type = TimeType.STOPWATCH
                )
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 0.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(
                        name = "segment name stopwatch",
                        type = TimeType.STOPWATCH
                    )
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 4.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(
                        name = "segment name rest",
                        type = TimeType.REST,
                        time = 4.seconds
                    )
                ),
                SegmentStep(
                    id = 2,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(
                        name = "segment name timer",
                        type = TimeType.TIMER,
                        time = 10.seconds
                    )
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.setup(state = state, routine = routine)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should setup state with same sound enable status when current state is counting`() {
        val segment = Segment.EMPTY.copy(time = 5.seconds)
        val routine = Routine.EMPTY.copy(
            preparationTime = 0.seconds,
            segments = listOf(segment)
        )
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 2.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = false
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = false
        )

        val actual = timerReducer.setup(state = state, routine = routine)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return error state on error`() {
        val exception = Exception()
        val expected = TimerState.Error(throwable = exception)

        val actual = timerReducer.error(throwable = exception)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return same state on progressing time when count is not running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(state)
    }

    @Test
    fun `should update state progressing the time when count is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 9.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state progressing time by counting down when running a starting segment`() {
        val segment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 9.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by completing the step on progressing time when a starting segment is running and count reach zero`() {
        val segment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds)
        val routine = Routine.EMPTY.copy(segments = listOf(segment), preparationTime = 5.seconds)
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by completing the step on progressing time when a timer segment is running and count reach zero`() {
        val segment = Segment.EMPTY.copy(type = TimeType.TIMER, time = 10.seconds)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by completing the step on progressing time when a rest segment is running and count reach zero`() {
        val segment = Segment.EMPTY.copy(type = TimeType.REST, time = 10.seconds)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by increasing the step count on progressing time when a stopwatch segment is running and count is zero`() {
        val segment = Segment.EMPTY.copy(type = TimeType.STOPWATCH, time = 0.seconds)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 0.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 1.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 0.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.progressTime(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return same state on toggling the timer when the count is completed`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.toggleTimeRunning(state)

        assertThat(actual).isEqualTo(state)
    }

    @Test
    fun `should update the state with completed count on toggling the timer when stopwatch segment is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.STOPWATCH)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.toggleTimeRunning(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state with count not running on toggle timer when a not stopwatch segment count is not completed and running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.toggleTimeRunning(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state resetting the step count on jump step back when a step count is not in the inital value`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.jumpStepBack(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by running the previous step on jump step back when segment count is in the initial value`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment), preparationTime = 5.seconds)
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 1,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.jumpStepBack(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by running the next step on jump step next when there is a next step`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment), preparationTime = 5.seconds)
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 1,
                count = Count(seconds = 10.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.jumpStepNext(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should update state by setting running step as completed on jump step next when there is not a next step`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment), preparationTime = 5.seconds)
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 1,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 1,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.jumpStepNext(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return the same state on next step when last segment is running`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.nextStep(state)

        assertThat(actual).isEqualTo(state)
    }

    @Test
    fun `should update state with new segment on next step`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment), preparationTime = 5.seconds)
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 1,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 5.seconds),
                    type = SegmentStepType.STARTING,
                    segment = segment
                ),
                SegmentStep(
                    id = 1,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.nextStep(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should return the same state on next step when routine is completed`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 0.seconds, isRunning = false, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )

        val actual = timerReducer.nextStep(state)

        assertThat(actual).isEqualTo(state)
    }

    @Test
    fun `should update state with sound disabled on toggling the sound`() {
        val segment = Segment.EMPTY.copy(time = 10.seconds, type = TimeType.TIMER)
        val routine = Routine.EMPTY.copy(segments = listOf(segment))
        val state = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = true
        )
        val expected = TimerState.Counting(
            routine = routine,
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 10.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = segment
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count.idle(seconds = 10.seconds),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = segment
                )
            ),
            isSoundEnabled = false
        )

        val actual = timerReducer.toggleSound(state)

        assertThat(actual).isEqualTo(expected)
    }
}
