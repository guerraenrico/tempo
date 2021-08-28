package com.enricog.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryField
import com.enricog.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoutineSummaryViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val converter: RoutineSummaryStateConverter = mockk()
    private val navigationActions: RoutinesNavigationActions = mockk(relaxUnitFun = true)
    private val reducer: RoutineSummaryReducer = mockk()
    private val routineSummaryUseCase: RoutineSummaryUseCase = mockk(relaxUnitFun = true)
    private val validator: RoutineSummaryValidator = mockk()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Before
    fun setup() {
        every { routineSummaryUseCase.get(routineId = any()) } returns flowOf(Routine.EMPTY)
        every {
            reducer.setup(routine = any())
        } returns RoutineSummaryState.Data(routine = Routine.EMPTY, errors = emptyMap())
        coEvery { converter.convert(any()) } returns RoutineSummaryViewState.Idle
    }

    @Test
    fun `should get routine on load`() = coroutineRule {
        buildSut()

        verify { routineSummaryUseCase.get(routineId = 1) }
    }

    @Test
    fun `should goToSegment on add new segment`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = 1),
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentAdd()

        coVerify { navigationActions.goToSegment(routineId = 1, segmentId = null) }
    }

    @Test
    fun `should goToSegment on segment selected`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = 1),
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentSelected(Segment.EMPTY.copy(id = 2))

        coVerify { navigationActions.goToSegment(routineId = 1, segmentId = 2) }
    }

    @Test
    fun `should update state and routine when segment is deleted`() = coroutineRule {
        val state = RoutineSummaryState.Data(routine = Routine.EMPTY, errors = emptyMap())
        val segment = Segment.EMPTY.copy(id = 1)
        every { reducer.setup(routine = any()) } returns state
        every { reducer.deleteSegment(state = any(), segment = any()) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentDelete(segment = segment)

        verify { reducer.deleteSegment(state = state, segment = segment) }
        coVerify { routineSummaryUseCase.update(routine = Routine.EMPTY) }
    }

    @Test
    fun `should goToTimer when starting routine without errors`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1)
        val state = RoutineSummaryState.Data(routine = routine, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every { validator.validate(routine = any()) } returns emptyMap()
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineStart()

        coVerify {
            validator.validate(routine = routine)
            navigationActions.goToTimer(routineId = 1)
        }
    }

    @Test
    fun `should apply errors to state when starting routine with errors`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1)
        val errors = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )
        val state = RoutineSummaryState.Data(routine = routine, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every { reducer.applyRoutineErrors(state = any(), errors = any()) } returns state
        every { validator.validate(routine = any()) } returns errors
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineStart()

        verify {
            validator.validate(routine = routine)
            reducer.applyRoutineErrors(state = state, errors = errors)
            navigationActions wasNot Called
        }
    }

    @Test
    fun `should goToRoutine on edit routine`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = 1),
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineEdit()

        coVerify { navigationActions.goToRoutine(routineId = 1) }
    }

    @Test
    fun `should goBack on back`() = coroutineRule {
        val sut = buildSut()

        sut.onBack()

        coVerify { navigationActions.goBackToRoutines() }
    }

    private fun buildSut(): RoutineSummaryViewModel {
        return RoutineSummaryViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            navigationActions = navigationActions,
            reducer = reducer,
            routineSummaryUseCase = routineSummaryUseCase,
            validator = validator
        )
    }
}
