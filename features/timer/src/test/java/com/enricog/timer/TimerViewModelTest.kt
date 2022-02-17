package com.enricog.timer

import androidx.lifecycle.SavedStateHandle
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.timer.models.Count
import com.enricog.timer.models.SegmentStep
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.usecase.TimerUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class TimerViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val converter: TimerStateConverter = mockk()
    private val reducer: TimerReducer = mockk()
    private val timerUseCase: TimerUseCase = mockk()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
    private val navigationActions: TimerNavigationActions = mockk(relaxUnitFun = true)
    private val windowScreenManager: WindowScreenManager = mockk(relaxUnitFun = true)

    @Before
    fun setup() {
        coEvery { timerUseCase.get(any()) } returns Routine.EMPTY
        coEvery { converter.convert(any()) } returns TimerViewState.Idle
        every { reducer.setup(any()) } returns TimerState.Idle
    }

    @Test
    fun `test onStartStopButtonClick`() = coroutineRule {
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut()

        advanceUntilIdle()
        sut.onStartStopButtonClick()

        verify { reducer.toggleTimeRunning(any()) }
    }

    @Test
    fun `test onRestartButtonClick`() = coroutineRule {
        every { reducer.restartTime(any()) } returns TimerState.Idle
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut()

        advanceUntilIdle()
        sut.onRestartSegmentButtonClick()

        verify { reducer.restartTime(any()) }
    }

    @Test
    fun `test onResetButtonClick should setup state again`() = coroutineRule {
        val countingState = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        every { reducer.progressTime(any()) } returns countingState
        every { reducer.toggleTimeRunning(any()) } returns countingState
        every { reducer.nextStep(any()) } returns countingState

        val sut = buildSut()

        advanceUntilIdle()
        sut.onResetButtonClick()

        verify { reducer.setup(any()) }
    }

    @Test
    fun `test every second should call progressTime`() = coroutineRule {
        val countingState = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        every { reducer.progressTime(any()) } returns TimerState.Idle
        every { reducer.toggleTimeRunning(any()) } returns countingState

        buildSut()

        advanceUntilIdle()

        verify { reducer.progressTime(any()) }
    }

    @Test
    fun `test onStateUpdated countingJob is cancelled when isCountCompleted`() = coroutineRule {
        val countingStateInitial = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        val countingStateCompleted = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(seconds = 5.seconds, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        every { reducer.nextStep(any()) } returns TimerState.Idle
        every { reducer.progressTime(any()) } returns countingStateCompleted
        every { reducer.toggleTimeRunning(any()) } returns countingStateInitial

        val sut = buildSut()

        advanceUntilIdle()

        verify { reducer.progressTime(any()) }
        verify { reducer.nextStep(any()) }
        assertTrue(sut.countingJob?.isCancelled ?: false)
    }

    @Test
    fun `test onDoneButtonClick should go backToRoutines`() = coroutineRule {
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut()

        advanceUntilIdle()
        sut.onDoneButtonClick()

        coVerify { navigationActions.backToRoutines() }
    }

    @Test
    fun `test onStateUpdated should toggleKeepScreenOnFlag(true) when segment count is running`() =
        coroutineRule {
            val countingStateInitial = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY,
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.STARTING
                )
            )
            val countingStateRunning = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
            every { reducer.progressTime(countingStateInitial) } returns countingStateRunning
            every { reducer.progressTime(countingStateRunning) } returns TimerState.Idle
            every { reducer.toggleTimeRunning(any()) } returns countingStateInitial

            buildSut()

            advanceUntilIdle()

            verify { windowScreenManager.toggleKeepScreenOnFlag(enable = true) }
        }

    @Test
    fun `test onStateUpdated should toggleKeepScreenOnFlag(false) when segment count is not running`() =
        coroutineRule {
            val countingStateInitial = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY,
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.STARTING
                )
            )
            val countingStateNotRunning = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = false, isCompleted = false),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
            every { reducer.progressTime(any()) } returns countingStateNotRunning
            every { reducer.toggleTimeRunning(any()) } returns countingStateInitial

            buildSut()

            advanceUntilIdle()

            verify { windowScreenManager.toggleKeepScreenOnFlag(enable = false) }
        }

    @Test
    fun `test onStateUpdated should toggleKeepScreenOnFlag(false) if routine is completed`() =
        coroutineRule {
            val countingStateInitial = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY,
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = true, isCompleted = false),
                    type = SegmentStepType.STARTING
                )
            )
            val countingStateRoutineComplete = TimerState.Counting(
                routine = Routine.EMPTY,
                runningSegment = Segment.EMPTY.copy(name = "segment name", type = TimeType.REST),
                step = SegmentStep(
                    count = Count(seconds = 5.seconds, isRunning = false, isCompleted = true),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
            every { reducer.progressTime(any()) } returns countingStateRoutineComplete
            every { reducer.toggleTimeRunning(any()) } returns countingStateInitial
            every { reducer.nextStep(any()) } returns TimerState.Idle

            buildSut()

            advanceUntilIdle()

            verify { windowScreenManager.toggleKeepScreenOnFlag(enable = false) }
        }

    @Test
    fun `test onCloseButtonClick should go backToRoutines`() = coroutineRule {
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut()

        advanceUntilIdle()
        sut.onCloseButtonClick()

        coVerify { navigationActions.backToRoutines() }
    }

    private fun buildSut(): TimerViewModel {
        return TimerViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            reducer = reducer,
            timerUseCase = timerUseCase,
            navigationActions = navigationActions,
            windowScreenManager = windowScreenManager
        )
    }
}
