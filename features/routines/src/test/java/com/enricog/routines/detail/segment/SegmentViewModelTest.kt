package com.enricog.routines.detail.segment

import androidx.lifecycle.SavedStateHandle
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.routines.detail.segment.models.SegmentField
import com.enricog.routines.detail.segment.models.SegmentFieldError
import com.enricog.routines.detail.segment.models.SegmentState
import com.enricog.routines.detail.segment.models.SegmentViewState
import com.enricog.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SegmentViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val converter: SegmentStateConverter = mockk()
    private val reducer: SegmentReducer = mockk()
    private val segmentUseCase: SegmentUseCase = mockk(relaxUnitFun = true)
    private val validator: SegmentValidator = mockk()
    private val navigationActions: RoutinesNavigationActions = mockk(relaxUnitFun = true)
    private val savedStateHandle = SavedStateHandle(
        mapOf(
            "routineId" to 1L,
            "segmentId" to 2L
        )
    )

    @Before
    fun setup() {
        coEvery { segmentUseCase.get(routineId = any()) } returns Routine.EMPTY
        // needed because right now mockk doesn't fully support value classes see https://github.com/mockk/mockk/issues/152
        coEvery { segmentUseCase.save(routine = any(), segment = any()) } returns Unit
        coEvery { converter.convert(state = any()) } returns SegmentViewState.Idle
        every {
            reducer.setup(routine = any(), segmentId = any())
        } returns SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )
    }

    @Test
    fun `should get routine and set segment on load `() = coroutineRule {
        coEvery { segmentUseCase.get(routineId = any()) } returns Routine.EMPTY
        buildSut()

        advanceUntilIdle()

        coVerify {
            segmentUseCase.get(routineId = 1.asID)
            reducer.setup(routine = Routine.EMPTY, segmentId = 2.asID)
        }
    }

    @Test
    fun `should update segment name onSegmentNameTextChange`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )
        every { reducer.setup(routine = any(), segmentId = any()) } returns state
        every { reducer.updateSegmentName(state = any(), text = any()) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentNameTextChange(text = "name")

        verify { reducer.updateSegmentName(state = state, text = "name") }
    }

    @Test
    fun `should update segment time onSegmentTimeChange`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )
        every { reducer.setup(routine = any(), segmentId = any()) } returns state
        every { reducer.updateSegmentTime(state = any(), seconds = 10.seconds) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentTimeChange(seconds = 10.seconds)

        verify { reducer.updateSegmentTime(state = state, seconds = 10.seconds) }
    }

    @Test
    fun `should update segment type onSegmentTypeChange`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )
        every { reducer.setup(routine = any(), segmentId = any()) } returns state
        every { reducer.updateSegmentTimeType(state = any(), timeType = any()) } returns state
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentTypeChange(timeType = TimeType.STOPWATCH)

        verify { reducer.updateSegmentTimeType(state = state, timeType = TimeType.STOPWATCH) }
    }

    @Test
    fun `should apply errors when segment has errors onSegmentConfirmed`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )
        val errors = mapOf<SegmentField, SegmentFieldError>(
            SegmentField.TimeInSeconds to SegmentFieldError.InvalidSegmentTime
        )
        every { reducer.setup(routine = any(), segmentId = any()) } returns state
        every { reducer.applySegmentErrors(state = any(), errors = any()) } returns state
        every { validator.validate(any()) } returns errors
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentConfirmed()

        verify {
            reducer.applySegmentErrors(state = state, errors = errors)
            navigationActions wasNot Called
        }
        coVerify(exactly = 0) { segmentUseCase.save(routine = any(), segment = any()) }
    }

    @Test
    fun `should save and go back when segment has no errors onSegmentConfirmed`() = coroutineRule {
        val state = SegmentState.Data(
            routine = Routine.EMPTY,
            segment = Segment.EMPTY,
            errors = emptyMap(),
            timeTypes = emptyList()
        )
        every { reducer.setup(routine = any(), segmentId = any()) } returns state
        every { validator.validate(any()) } returns emptyMap()
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentConfirmed()

        coVerify {
            segmentUseCase.save(routine = Routine.EMPTY, segment = Segment.EMPTY)
            navigationActions.goBack()
        }
        verify(exactly = 0) { reducer.applySegmentErrors(state = any(), errors = any()) }
    }

    private fun buildSut(): SegmentViewModel {
        return SegmentViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            reducer = reducer,
            segmentUseCase = segmentUseCase,
            validator = validator,
            navigationActions = navigationActions
        )
    }
}
