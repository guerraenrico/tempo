package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
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

    private val navigator = FakeNavigator()

    private val converter: RoutineSummaryStateConverter = mockk()
    private val reducer: RoutineSummaryReducer = mockk()
    private val routineSummaryUseCase: RoutineSummaryUseCase = mockk(relaxUnitFun = true)
    private val validator: RoutineSummaryValidator = mockk()
    private val moveSegmentUseCase: MoveSegmentUseCase = mockk()
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

        verify { routineSummaryUseCase.get(routineId = 1.asID) }
    }

    @Test
    fun `should goToSegment on add new segment`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = 1.asID),
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()

        sut.onSegmentAdd()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = 1.asID, segmentId = ID.new())
        )
    }

    @Test
    fun `should goToSegment on segment selected`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = 1.asID),
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()

        sut.onSegmentSelected(Segment.EMPTY.copy(id = 2.asID))

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = 1.asID, segmentId = 2.asID)
        )
    }

    @Test
    fun `should update state and routine when segment is deleted`() = coroutineRule {
        val state = RoutineSummaryState.Data(routine = Routine.EMPTY, errors = emptyMap())
        val segment = Segment.EMPTY.copy(id = 1.asID)
        every { reducer.setup(routine = any()) } returns state
        every { reducer.deleteSegment(state = any(), segment = any()) } returns state
        val sut = buildSut()

        sut.onSegmentDelete(segment = segment)

        verify { reducer.deleteSegment(state = state, segment = segment) }
        coVerify { routineSummaryUseCase.update(routine = Routine.EMPTY) }
    }

    @Test
    fun `should move segment when segment is moved`() = coroutineRule {
        val segment1 = Segment.EMPTY.copy(id = ID.from(1), rank = Rank.from("bbbbbb"))
        val segment2 = Segment.EMPTY.copy(id = ID.from(2), rank = Rank.from("cccccc"))
        val routine = Routine.EMPTY.copy(id = 1.asID, segments = listOf(segment1, segment2))
        val state = RoutineSummaryState.Data(
            routine = routine,
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()

        sut.onSegmentMoved(segment = segment1, hoveredSegment = segment2)

        coVerify {
            moveSegmentUseCase(
                routine = routine,
                segment = segment1,
                hoveredSegment = segment2
            )
        }
    }

    @Test
    fun `should goToTimer when starting routine without errors`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryState.Data(routine = routine, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every { validator.validate(routine = any()) } returns emptyMap()
        val sut = buildSut()

        sut.onRoutineStart()

        coVerify {
            validator.validate(routine = routine)
        }

        navigator.assertGoTo(
            route = TimerRoute,
            input = TimerRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should apply errors to state when starting routine with errors`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)
        val errors = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )
        val state = RoutineSummaryState.Data(routine = routine, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every { reducer.applyRoutineErrors(state = any(), errors = any()) } returns state
        every { validator.validate(routine = any()) } returns errors
        val sut = buildSut()

        sut.onRoutineStart()

        verify {
            validator.validate(routine = routine)
            reducer.applyRoutineErrors(state = state, errors = errors)
        }

        navigator.assertNoActions()
    }

    @Test
    fun `should goToRoutine on edit routine`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(id = 1.asID),
            errors = emptyMap()
        )
        every { reducer.setup(routine = any()) } returns state
        val sut = buildSut()

        sut.onRoutineEdit()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should goBack on back`() = coroutineRule {
        val sut = buildSut()

        sut.onBack()

        navigator.assertGoBack()
    }

    private fun buildSut(): RoutineSummaryViewModel {
        return RoutineSummaryViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = reducer,
            routineSummaryUseCase = routineSummaryUseCase,
            moveSegmentUseCase = moveSegmentUseCase,
            validator = validator
        )
    }
}
