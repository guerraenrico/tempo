package com.enricog.timer

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.timer.models.*
import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.usecase.TimerUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    private val configuration = TimerConfiguration(routineId = 1)
    private val navigationActions: TimerNavigationActions = mockk(relaxUnitFun = true)

    @Before
    fun setup() {
        coEvery { timerUseCase.get(any()) } returns Routine.EMPTY
        coEvery { converter.convert(any()) } returns TimerViewState.Idle
        every { reducer.setup(any()) } returns TimerState.Idle
    }

    @Test
    fun `test onStartStopButtonClick`() = coroutineRule {
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut().apply { load(configuration) }

        advanceUntilIdle()
        sut.onStartStopButtonClick()

        verify { reducer.toggleTimeRunning(any()) }
    }

    @Test
    fun `test onRestartButtonClick`() = coroutineRule {
        every { reducer.restartTime(any()) } returns TimerState.Idle
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut().apply { load(configuration) }

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
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.IN_PROGRESS
            )
        )
        every { reducer.progressTime(any()) } returns countingState
        every { reducer.toggleTimeRunning(any()) } returns countingState
        every { reducer.nextStep(any()) } returns countingState

        val sut = buildSut().apply { load(configuration) }

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
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        every { reducer.progressTime(any()) } returns TimerState.Idle
        every { reducer.toggleTimeRunning(any()) } returns countingState

        buildSut().apply { load(configuration) }

        advanceUntilIdle()

        verify { reducer.progressTime(any()) }
    }

    @Test
    fun `test onStateUpdated countingJob is cancelled when isCountCompleted`() = coroutineRule {
        val countingStateInitial = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = false),
                type = SegmentStepType.STARTING
            )
        )
        val countingStateCompleted = TimerState.Counting(
            routine = Routine.EMPTY,
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(timeInSeconds = 5, isRunning = true, isCompleted = true),
                type = SegmentStepType.STARTING
            )
        )
        every { reducer.nextStep(any()) } returns TimerState.Idle
        every { reducer.progressTime(any()) } returns countingStateCompleted
        every { reducer.toggleTimeRunning(any()) } returns countingStateInitial

        val sut = buildSut().apply { load(configuration) }

        advanceUntilIdle()

        verify { reducer.progressTime(any()) }
        verify { reducer.nextStep(any()) }
        assertTrue(sut.countingJob?.isCancelled ?: false)
    }

    @Test
    fun `test onDoneButtonClick should go backToRoutines`() = coroutineRule {
        every { reducer.toggleTimeRunning(any()) } returns TimerState.Idle

        val sut = buildSut().apply { load(configuration) }

        advanceUntilIdle()
        sut.onDoneButtonClick()

        verify { navigationActions.backToRoutines() }
    }

    private fun buildSut(): TimerViewModel {
        return TimerViewModel(
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            reducer = reducer,
            timerUseCase = timerUseCase,
            navigationActions = navigationActions
        )
    }
}