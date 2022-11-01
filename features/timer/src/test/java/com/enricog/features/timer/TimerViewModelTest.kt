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
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.timer.fakes.FakeWindowScreenManager
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.navigation.TimerNavigationActions
import com.enricog.features.timer.usecase.TimerUseCase
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.theme.TimeTypeColors
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TimerViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val firstSegment = Segment.EMPTY.copy(
        id = 1.asID,
        name = "First Segment",
        time = 5.seconds,
        type = TimeType.TIMER
    )
    private val secondSegment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Second Segment",
        time = 4.seconds,
        type = TimeType.TIMER
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        segments = listOf(firstSegment, secondSegment),
        startTimeOffset = 3.seconds
    )

    private val navigator = FakeNavigator()
    private val store = FakeStore(listOf(routine))
    private val windowScreenManager = FakeWindowScreenManager()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Test
    fun `on init should setup routine and start count down`() = coroutineRule {
        val expectedOnSetup = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 3.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            stepTitleId = R.string.title_segment_step_type_starting,
            segmentName = "First Segment",
            clockBackgroundColor = TimeTypeColors.STARTING,
            isRoutineCompleted = false,
        )
        val expectedOnStart = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 3.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            stepTitleId = R.string.title_segment_step_type_starting,
            segmentName = "First Segment",
            clockBackgroundColor = TimeTypeColors.STARTING,
            isRoutineCompleted = false,
        )
        val sut = buildSut()

        sut.viewState.test {
            advanceTimeBy(100)
            assertEquals(expectedOnSetup, awaitItem())
            advanceTimeBy(1000)
            assertEquals(expectedOnStart, awaitItem())
            advanceTimeBy(1000)

            windowScreenManager.keepScreenOn.test { assertEquals(true, awaitItem()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should stop routine on toggle when timer is running`() = coroutineRule {
        val expected = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 2.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            stepTitleId = R.string.title_segment_step_type_starting,
            segmentName = "First Segment",
            clockBackgroundColor = TimeTypeColors.STARTING,
            isRoutineCompleted = false,
        )
        val sut = buildSut()

        sut.viewState.test {
            advanceTimeBy(3000)

            sut.onToggleTimer()

            advanceTimeBy(100)
            assertEquals(expected, expectMostRecentItem())
            advanceTimeBy(100)
            windowScreenManager.keepScreenOn.test { assertFalse(awaitItem()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should restart timer when restart`() = coroutineRule {
        val expected = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 3.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            stepTitleId = R.string.title_segment_step_type_starting,
            segmentName = "First Segment",
            clockBackgroundColor = TimeTypeColors.STARTING,
            isRoutineCompleted = false,
        )
        val sut = buildSut()

        sut.viewState.test {
            advanceTimeBy(3000)

            sut.onReset()

            advanceTimeBy(100)
            assertEquals(expected, expectMostRecentItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should restart routine on reset`() = coroutineRule {
        val expectedStart = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 3.seconds, isRunning = false, isCompleted = false),
                type = SegmentStepType.STARTING
            ),
            stepTitleId = R.string.title_segment_step_type_starting,
            segmentName = "First Segment",
            clockBackgroundColor = TimeTypeColors.STARTING,
            isRoutineCompleted = false,
        )
        val expectedStartFirstSegment = TimerViewState.Counting(
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.IN_PROGRESS
            ),
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "First Segment",
            clockBackgroundColor = TimeTypeColors.TIMER,
            isRoutineCompleted = false,
        )
        val sut = buildSut()

        sut.viewState.test {
            advanceTimeBy(5000)

            advanceTimeBy(1000)
            assertEquals(expectedStartFirstSegment, expectMostRecentItem())

            sut.onReset()

            advanceTimeBy(100)
            assertEquals(expectedStart, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `on done should go to routines`() = coroutineRule {
        val sut = buildSut()

        sut.onDone()

        navigator.assertGoTo(route = RoutinesRoute, input = RoutinesRouteInput)
    }

    @Test
    fun `should go to routines on close`() = coroutineRule {
        val sut = buildSut()

        sut.onClose()

        navigator.assertGoTo(route = RoutinesRoute, input = RoutinesRouteInput)
    }

    private fun buildSut(): TimerViewModel {
        return TimerViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = TimerStateConverter(),
            reducer = TimerReducer(),
            timerUseCase = TimerUseCase(routineDataSource = FakeRoutineDataSource(store = store)),
            navigationActions = TimerNavigationActions(navigator),
            windowScreenManager = windowScreenManager
        )
    }
}
