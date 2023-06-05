package com.enricog.features.timer

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.settings.FakeTimerSettingsDataSource
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.data.timer.testing.theme.FakeTimerThemeDataSource
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.timer.fakes.FakeTimerProvider
import com.enricog.features.timer.fakes.FakeTimerServiceHandler
import com.enricog.features.timer.fakes.FakeWindowScreenManager
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.Background
import com.enricog.features.timer.models.TimerViewState.Idle
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.usecase.GetRoutineUseCase
import com.enricog.features.timer.usecase.GetTimerSettingsUseCase
import com.enricog.features.timer.usecase.GetTimerThemeUseCase
import com.enricog.libraries.permission.api.Permission
import com.enricog.libraries.permission.testing.FakePermissionManager
import com.enricog.libraries.sound.testing.FakeSoundPlayer
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.api.routes.TimerSettingsRoute
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class TimerViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    @get:Rule
    val coroutineRule = CoroutineRule(testCoroutineDispatcher)

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val timerSettings = TimerSettings.DEFAULT
    private val timerTheme = TimerTheme.DEFAULT
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
    private val routinesStore = FakeStore(listOf(routine))
    private val timerSettingsStore = FakeStore(timerSettings)
    private val timerThemeStore = FakeStore(listOf(timerTheme))
    private val windowScreenManager = FakeWindowScreenManager()
    private val soundPlayer = FakeSoundPlayer()
    private val timerServiceHandler = FakeTimerServiceHandler()
    private val permissionManager = FakePermissionManager()
    private val timerProvider = FakeTimerProvider(testCoroutineScope)
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Before
    fun setup() {
        timerSettingsStore.put(timerSettings)
        permissionManager.setPermission(Permission.NOTIFICATION to true)
    }

    @After
    fun reset() {
        soundPlayer.close()
    }

    @Test
    fun `on init should setup routine and start count down`() = coroutineRule {
        val expectedOnSetup = buildCountingState {
            timerActions = buildTimerActions {
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            }
        }
        val expectedOnStart = buildCountingState {
            timerActions = buildTimerActions {
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_stop,
                    contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            }
        }
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(Idle)
            // Load
            runCurrent()
            assertThat(awaitItem()).isEqualTo(expectedOnSetup)
            // Start
            advanceTimeBy(1000)
            runCurrent()
            assertThat(awaitItem()).isEqualTo(expectedOnStart)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should stop timer count on toggle when timer is running`() = coroutineRule {
        val expected = buildCountingState {
            timeInSeconds = 2
            timerActions = buildTimerActions {
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            }
        }
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()
            // Advance count time 1 second
            advanceTimeBy(1000)
            runCurrent()

            viewModel.onPlay()
            runCurrent()

            assertThat(expectMostRecentItem()).isEqualTo(expected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should reset step count on back`() = coroutineRule {
        val expected = buildCountingState {
            timeInSeconds = 3
            timerActions = buildTimerActions {
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            }
        }
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()
            // Advance count time 1 second
            advanceTimeBy(1000)
            runCurrent()

            viewModel.onBack()
            runCurrent()

            assertThat(expectMostRecentItem()).isEqualTo(expected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should run next step on next`() = coroutineRule {
        val expected = buildCountingState {
            timeInSeconds = 10
            stepTitleId = R.string.title_segment_step_type_in_progress
            segmentName = "First Segment"
            clockBackground = Background(background = Color.Red, ripple = null)
            timerActions = buildTimerActions {
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            }
        }
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()
            // Advance count time 1 second
            advanceTimeBy(1000)
            runCurrent()

            viewModel.onNext()
            runCurrent()

            assertThat(expectMostRecentItem()).isEqualTo(expected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should restart routine on reset`() = coroutineRule {
        val expected = buildCountingState {
            timeInSeconds = 3
            timerActions = buildTimerActions {
                play = TimerViewState.Counting.Actions.Button(
                    iconResId = R.drawable.ic_timer_play,
                    contentDescriptionResId = R.string.content_description_button_start_routine_segment,
                    size = TempoIconButtonSize.Normal
                )
            }
        }
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(Idle)
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()
            // Advance count time 1 second
            advanceTimeBy(1000)
            runCurrent()

            viewModel.onReset()
            runCurrent()

            assertThat(expectMostRecentItem()).isEqualTo(expected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should keep screen on when keep screen on setting is enabled`() = coroutineRule {
        timerSettingsStore.put(timerSettings.copy(keepScreenOnEnabled = true))
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()
            // Advance count time 1 second
            advanceTimeBy(1000)
            runCurrent()

            windowScreenManager.keepScreenOn.test {
                assertThat(expectMostRecentItem()).isTrue()
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should not keep screen on when keep screen on setting is disabled`() = coroutineRule {
        timerSettingsStore.put(timerSettings.copy(keepScreenOnEnabled = false))
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()
            // Advance count time 1 second
            advanceTimeBy(1000)
            runCurrent()

            windowScreenManager.keepScreenOn.test {
                assertThat(expectMostRecentItem()).isFalse()
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should play sounds when segment count is completing and sound is enabled`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()

            // Preparation time
            soundPlayer.assertSoundPlayed(soundResId = R.raw.speech_prepare, times = 1)

            // Advance count time 3 second
            advanceTimeBy(3000)
            // When segment starting is completing should play sounds
            soundPlayer.assertSoundPlayed(soundResId = R.raw.sound_count_down, times = 2)

            // Advance count time 1 second
            advanceTimeBy(1000)
            soundPlayer.assertSoundPlayed(soundResId = R.raw.sound_count_down_end, times = 1)

            // Advance count time 1 second
            advanceTimeBy(1000)
            soundPlayer.assertSoundPlayed(soundResId = R.raw.speech_go, times = 1)
            soundPlayer.close()

            // Start first segment (10 seconds)
            // Advance count time 2 seconds (cumulative count seconds: 2)
            advanceTimeBy(2000)
            // When segment count is not completing should not play sounds
            soundPlayer.assertNoSoundHasPlayed()
            soundPlayer.close()
            runCurrent()

            // Advance count time 7 seconds (cumulative count seconds: 9)
            advanceTimeBy(7000)
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
        timerSettingsStore.put(timerSettings.copy(soundEnabled = false))
        val viewModel = buildViewModel()

        viewModel.viewState.test {
            // Load
            runCurrent()
            // Start
            advanceTimeBy(1000)
            runCurrent()

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
    fun `should start service when timer is counting and run in background setting is enabled`() = coroutineRule {
        timerSettingsStore.put(TimerSettings.DEFAULT.copy(runInBackgroundEnabled = true))

        buildViewModel()
        runCurrent()

        timerServiceHandler.assertServiceIsStarted()
    }

    @Test
    fun `should not start service when run in background setting is disabled`() = coroutineRule {
        timerSettingsStore.put(TimerSettings.DEFAULT.copy(runInBackgroundEnabled = false))

        buildViewModel()
        runCurrent()

        timerServiceHandler.assertServiceIsNotStarted()
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

    @Test
    fun `should go to timer setting`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onShowSettings()

        navigator.assertGoTo(route = TimerSettingsRoute, input = TimerSettingsRoute.Input)
    }


    private fun buildViewModel(): TimerViewModel {
        return TimerViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = TimerStateConverter(clock = clock),
            timerController = TimerController(
                timerProvider = timerProvider,
                scope = testCoroutineScope,
                reducer = TimerReducer(clock = clock),
                soundPlayer = TimerSoundPlayer(soundPlayer = soundPlayer)
            ),
            navigationActions = TimerNavigationActions(navigator = navigator),
            getRoutineUseCase = GetRoutineUseCase(
                routineDataSource = FakeRoutineDataSource(store = routinesStore)
            ),
            getTimerThemeUseCase = GetTimerThemeUseCase(
                timerThemeDataSource = FakeTimerThemeDataSource(
                    store = timerThemeStore
                )
            ),
            getTimerSettingsUseCase = GetTimerSettingsUseCase(
                timerSettingsDataSource = FakeTimerSettingsDataSource(
                    store = timerSettingsStore
                ),
                permissionManager = permissionManager
            ),
            windowScreenManager = windowScreenManager,
            serviceHandler = timerServiceHandler
        )
    }

    private fun buildCountingState(block: CountingStateBuilder.() -> Unit): TimerViewState.Counting {
        val builder = CountingStateBuilder()
        builder.block()
        return builder.build()
    }

    private fun buildTimerActions(block: TimerActionBuilder.() -> Unit): TimerViewState.Counting.Actions {
        val builder = TimerActionBuilder()
        builder.block()
        return builder.build()
    }

    private class CountingStateBuilder {
        var timeInSeconds: Long = 3
        var stepTitleId: Int = R.string.title_segment_step_type_preparation
        var segmentName: String = "First Segment"
        var clockBackground: Background = Background(
            background = Color.Blue,
            ripple = null
        )
        var clockOnBackgroundColor: Color = Color.White
        var timerActions: TimerViewState.Counting.Actions = TimerActionBuilder().build()

        fun build(): TimerViewState.Counting {
            return TimerViewState.Counting(
                timeInSeconds = timeInSeconds,
                stepTitleId = stepTitleId,
                segmentName = segmentName,
                clockBackground = clockBackground,
                clockOnBackgroundColor = clockOnBackgroundColor,
                timerActions = timerActions
            )
        }
    }

    private class TimerActionBuilder {
        var next: TimerViewState.Counting.Actions.Button = TimerViewState.Counting.Actions.Button(
            iconResId = R.drawable.ic_timer_next,
            contentDescriptionResId = R.string.content_description_button_next_routine_segment,
            size = TempoIconButtonSize.Normal
        )
        var back: TimerViewState.Counting.Actions.Button = TimerViewState.Counting.Actions.Button(
            iconResId = R.drawable.ic_timer_back,
            contentDescriptionResId = R.string.content_description_button_back_routine_segment,
            size = TempoIconButtonSize.Normal
        )
        var play: TimerViewState.Counting.Actions.Button = TimerViewState.Counting.Actions.Button(
            iconResId = R.drawable.ic_timer_stop,
            contentDescriptionResId = R.string.content_description_button_stop_routine_segment,
            size = TempoIconButtonSize.Normal
        )

        fun build(): TimerViewState.Counting.Actions {
            return TimerViewState.Counting.Actions(next = next, back = back, play = play)
        }
    }

}
