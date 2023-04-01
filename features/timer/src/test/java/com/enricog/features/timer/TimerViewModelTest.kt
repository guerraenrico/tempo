package com.enricog.features.timer

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.features.timer.fakes.FakeWindowScreenManager
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
import com.enricog.features.timer.models.TimerViewState.Idle
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.usecase.TimerUseCase
import com.enricog.libraries.sound.testing.FakeSoundPlayer
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.theme.TimeTypeColors
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class TimerViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val firstSegment = Segment.EMPTY.copy(
        id = 1.asID,
        name = "First Segment",
        time = 10.seconds,
        type = TimeType.TIMER
    )
    private val secondSegment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Second Segment",
        time = 8.seconds,
        type = TimeType.TIMER
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        segments = listOf(firstSegment, secondSegment),
        preparationTime = 3.seconds
    )

    private val navigator = FakeNavigator()
    private val store = FakeStore(listOf(routine))
    private val windowScreenManager = FakeWindowScreenManager()
    private val soundPlayer = FakeSoundPlayer()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @After
    fun reset() {
        soundPlayer.close()
    }

    @Test
    fun `on init should setup routine and start count down`() = coroutineRule {
        val expectedOnSetup = TimerViewState.Counting(
            timeInSeconds = 3,
            stepTitleId = R.string.title_segment_step_type_preparation,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.STARTING,
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
        val expectedOnStart = TimerViewState.Counting(
            timeInSeconds = 3,
            stepTitleId = R.string.title_segment_step_type_preparation,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.STARTING,
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
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            runCurrent()
            assertThat(awaitItem()).isEqualTo(Idle)
            assertThat(awaitItem()).isEqualTo(expectedOnSetup)
            advanceTimeBy(1000)
            runCurrent()
            assertThat(awaitItem()).isEqualTo(expectedOnStart)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should play sounds when segment count is completing and sound is enabled`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            runCurrent()
            advanceTimeBy(1000)
            runCurrent()

            // Preparation time
            soundPlayer.assertSoundPlayed(soundResId = R.raw.speech_prepare, times = 1)
            advanceTimeBy(3000)
            // When segment starting is completing should play sounds
            soundPlayer.assertSoundPlayed(soundResId = R.raw.sound_count_down, times = 2)
            advanceTimeBy(1000)
            soundPlayer.assertSoundPlayed(soundResId = R.raw.sound_count_down_end, times = 1)
            advanceTimeBy(1000)
            soundPlayer.assertSoundPlayed(soundResId = R.raw.speech_go, times = 1)
            soundPlayer.close()

            // Start first segment
            advanceTimeBy(4000)
            // When segment count is not completing should not play sounds
            soundPlayer.assertNoSoundHasPlayed()
            soundPlayer.close()

            // Complete first segment
            advanceTimeBy(5000)
            // When segment count is completing should play sounds
            soundPlayer.assertSoundPlayed(soundResId = R.raw.sound_count_down, times = 5)
            advanceTimeBy(1000)
            soundPlayer.assertSoundPlayed(soundResId = R.raw.sound_count_down_end, times = 1)
            advanceTimeBy(1000)
            soundPlayer.assertSoundPlayed(soundResId = R.raw.speech_prepare, times = 1)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should not play sounds when segment count is completing and sound is disabled`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            runCurrent()
            advanceTimeBy(1000)

            viewModel.onPlay()
            runCurrent()

            // Preparation time
            soundPlayer.assertNoSoundHasPlayed()
            advanceTimeBy(3000)
            soundPlayer.assertNoSoundHasPlayed()
            advanceTimeBy(1000)
            soundPlayer.assertNoSoundHasPlayed()
            advanceTimeBy(1000)
            soundPlayer.assertNoSoundHasPlayed()

            // Start first segment
            advanceTimeBy(4000)
            soundPlayer.assertNoSoundHasPlayed()

            // Complete first segment
            advanceTimeBy(5000)
            soundPlayer.assertNoSoundHasPlayed()
            advanceTimeBy(1000)
            soundPlayer.assertNoSoundHasPlayed()
            advanceTimeBy(1000)
            soundPlayer.assertNoSoundHasPlayed()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should stop routine on toggle when timer is running`() = coroutineRule {
        val expected = TimerViewState.Counting(
            timeInSeconds = 2,
            stepTitleId = R.string.title_segment_step_type_preparation,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.STARTING,
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
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            advanceTimeBy(3000)

            viewModel.onPlay()

            runCurrent()
            assertThat(expectMostRecentItem()).isEqualTo(expected)
            windowScreenManager.keepScreenOn.test {
                assertThat(awaitItem()).isFalse()
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should reset step count on back`() = coroutineRule {
        val expected = TimerViewState.Counting(
            timeInSeconds = 3,
            stepTitleId = R.string.title_segment_step_type_preparation,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.STARTING,
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
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            runCurrent()
            advanceTimeBy(3000)

            viewModel.onBack()

            runCurrent()
            assertThat(expectMostRecentItem()).isEqualTo(expected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should run next step on next`() = coroutineRule {
        val expected = TimerViewState.Counting(
            timeInSeconds = 10,
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.TIMER,
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
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            runCurrent()
            advanceTimeBy(3000)

            viewModel.onNext()

            runCurrent()
            assertThat(expectMostRecentItem()).isEqualTo(expected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should restart routine on reset`() = coroutineRule {
        val expectedStart = TimerViewState.Counting(
            timeInSeconds = 3,
            stepTitleId = R.string.title_segment_step_type_preparation,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.STARTING,
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
        val expectedStartFirstSegment = TimerViewState.Counting(
            timeInSeconds = 10,
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "First Segment",
            clockBackgroundColor = BackgroundColor(
                background = TimeTypeColors.TIMER,
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
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            runCurrent()
            advanceTimeBy(5000)

            advanceTimeBy(1000)
            assertThat(expectMostRecentItem()).isEqualTo(expectedStartFirstSegment)

            viewModel.onReset()

            runCurrent()
            assertThat(awaitItem()).isEqualTo(expectedStart)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should go to routines on done`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onDone()

        navigator.assertGoTo(route = RoutinesRoute, input = RoutinesRouteInput)
    }

    @Test
    fun `should go to routines on close`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onClose()

        navigator.assertGoTo(route = RoutinesRoute, input = RoutinesRouteInput)
    }

    private fun buildViewModel(): TimerViewModel {
        return TimerViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = TimerStateConverter(clock = clock),
            reducer = TimerReducer(clock = clock),
            timerUseCase = TimerUseCase(routineDataSource = FakeRoutineDataSource(store = store)),
            navigationActions = TimerNavigationActions(navigator = navigator),
            windowScreenManager = windowScreenManager,
            soundPlayer = TimerSoundPlayer(soundPlayer = soundPlayer)
        )
    }
}
