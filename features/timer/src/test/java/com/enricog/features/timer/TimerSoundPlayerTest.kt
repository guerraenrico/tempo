package com.enricog.features.timer

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.seconds
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.libraries.sound.testing.FakeSoundPlayer
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@RunWith(TestParameterInjector::class)
internal class TimerSoundPlayerTest {

    private val soundPlayer = FakeSoundPlayer()
    private val timerSoundPlayer = TimerSoundPlayer(soundPlayer = soundPlayer)

    @After
    fun reset() {
        soundPlayer.close()
    }

    @Suppress("unused")
    enum class NotCountingStateTestCase(val state: TimerState) {
        IDLE(state = TimerState.Idle),
        ERROR(state = TimerState.Error(throwable = Exception())),
    }

    @Test
    fun `should not play sound when state is not counting`(@TestParameter testCase: NotCountingStateTestCase) {
        timerSoundPlayer.playFrom(state = testCase.state)

        soundPlayer.assertNoSoundHasPlayed()
    }

    @Test
    fun `should not play sound when sound is disabled`() {
        val state = baseCountingState.copy(isSoundEnabled = false)

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertNoSoundHasPlayed()
    }

    @Test
    fun `should play sound when routine is completed`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps.last().copy(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.sound_routine_complete)
    }

    @Test
    fun `should play sound when step count is completing`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps.first().copy(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.sound_count_down)
    }

    @Test
    fun `should play sound when step count is completed`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps.last().copy(
                count = Count(seconds = 0.seconds, isRunning = true, isCompleted = true),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.sound_routine_complete)
    }

    @Test
    fun `should play sound when step rest count is started`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps.last().copy(
                count = Count.start(seconds = 3000.seconds),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.speech_rest)
    }

    @Test
    fun `should play sound when step preparation count is started`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps.first().copy(
                count = Count.start(seconds = 30.seconds),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.speech_prepare)
    }

    @Test
    fun `should play sound when step timer count is started`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps[1].copy(
                count = Count.start(seconds = 3000.seconds),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.speech_go)
    }

    @Test
    fun `should play sound when step stopwatch count is started`() {
        val state = baseCountingState.copy(
            runningStep = baseCountingState.steps[3].copy(
                count = Count.start(seconds = 0.seconds),
            )
        )

        timerSoundPlayer.playFrom(state = state)

        soundPlayer.assertSoundPlayed(R.raw.speech_go)
    }

    @Suppress("unused")
    enum class PreparationStepTestCase(val state: TimerState.Counting, val expectedSound: Int) {
        COUNT_SECS_15(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps.first().copy(count = Count.start(seconds = 15.seconds))
            ),
            expectedSound = R.raw.speech_15_secs
        )
    }

    @Test
    fun `should speech when step count is at specifics seconds and segment is preparation`(@TestParameter testCase: PreparationStepTestCase) {
        timerSoundPlayer.playFrom(state = testCase.state)

        soundPlayer.assertSoundPlayed(testCase.expectedSound)
    }

    @Suppress("unused")
    enum class TimerStepTestCase(val state: TimerState.Counting, val expectedSound: Int) {
        COUNT_SECS_15(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 15.seconds))
            ),
            expectedSound = R.raw.speech_15_secs
        ),
        COUNT_MIN_1(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 60.seconds))
            ),
            expectedSound = R.raw.speech_1_min
        ),
        COUNT_MIN_5(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 300.seconds))
            ),
            expectedSound = R.raw.speech_5_mins
        ),
        COUNT_MIN_10(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 600.seconds))
            ),
            expectedSound = R.raw.speech_10_mins
        ),
        COUNT_MIN_15(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 900.seconds))
            ),
            expectedSound = R.raw.speech_15_mins
        ),
        COUNT_MIN_30(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 1800.seconds))
            ),
            expectedSound = R.raw.speech_30_mins
        ),
        COUNT_MIN_45(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[1].copy(count = Count.start(seconds = 2700.seconds))
            ),
            expectedSound = R.raw.speech_45_mins
        )
    }

    @Test
    fun `should speech when step count is at specifics seconds and segment is timer in progress`(@TestParameter testCase: TimerStepTestCase) {
        timerSoundPlayer.playFrom(state = testCase.state)

        soundPlayer.assertSoundPlayed(testCase.expectedSound)
    }

    @Suppress("unused")
    enum class RestStepTestCase(val state: TimerState.Counting, val expectedSound: Int) {
        COUNT_SECS_15(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 15.seconds))
            ),
            expectedSound = R.raw.speech_15_secs
        ),
        COUNT_MIN_1(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 60.seconds))
            ),
            expectedSound = R.raw.speech_1_min
        ),
        COUNT_MIN_5(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 300.seconds))
            ),
            expectedSound = R.raw.speech_5_mins
        ),
        COUNT_MIN_10(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 600.seconds))
            ),
            expectedSound = R.raw.speech_10_mins
        ),
        COUNT_MIN_15(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 900.seconds))
            ),
            expectedSound = R.raw.speech_15_mins
        ),
        COUNT_MIN_30(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 1800.seconds))
            ),
            expectedSound = R.raw.speech_30_mins
        ),
        COUNT_MIN_45(
            state = baseCountingState.copy(
                runningStep = baseCountingState.steps[2].copy(count = Count.start(seconds = 2700.seconds))
            ),
            expectedSound = R.raw.speech_45_mins
        )
    }

    @Test
    fun `should speech when step count is at specifics seconds and segment is rest in progress`(@TestParameter testCase: RestStepTestCase) {
        timerSoundPlayer.playFrom(state = testCase.state)

        soundPlayer.assertSoundPlayed(testCase.expectedSound)
    }

    private companion object {
        val baseCountingState = TimerState.Counting(
            routine = Routine.EMPTY.copy(
                segments = listOf(
                    Segment.EMPTY.copy(name = "segment timer", type = TimeType.TIMER, time = 3000.seconds),
                    Segment.EMPTY.copy(name = "segment stopwatch", type = TimeType.STOPWATCH, time = 0.seconds),
                    Segment.EMPTY.copy(name = "segment rest", type = TimeType.REST, time = 3000.seconds)
                ),
                preparationTime = 30.seconds
            ),
            runningStep = SegmentStep(
                id = 0,
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS,
                segment = Segment.EMPTY.copy(name = "segment timer", type = TimeType.TIMER, time = 3000.seconds)
            ),
            steps = listOf(
                SegmentStep(
                    id = 0,
                    count = Count(seconds = 30.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(name = "segment timer", type = TimeType.TIMER, time = 3000.seconds)
                ),
                SegmentStep(
                    id = 1,
                    count = Count(seconds = 3000.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment timer", type = TimeType.TIMER, time = 3000.seconds)
                ),
                SegmentStep(
                    id = 2,
                    count = Count(seconds = 30.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.PREPARATION,
                    segment = Segment.EMPTY.copy(name = "segment stopwatch", type = TimeType.STOPWATCH, time = 0.seconds)
                ),
                SegmentStep(
                    id = 3,
                    count = Count(seconds = 3000.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment stopwatch", type = TimeType.STOPWATCH, time = 0.seconds)
                ),
                SegmentStep(
                    id = 4,
                    count = Count(seconds = 3000.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS,
                    segment = Segment.EMPTY.copy(name = "segment rest", type = TimeType.REST, time = 3000.seconds)
                )
            ),
            isSoundEnabled = true,
            startedAt = OffsetDateTime.now(Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))),
            skipCount = 0,
            timerTheme = TimerTheme.DEFAULT
        )
    }
}