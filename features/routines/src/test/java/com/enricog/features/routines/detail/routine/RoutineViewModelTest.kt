package com.enricog.features.routines.detail.routine

import androidx.lifecycle.SavedStateHandle
import com.enricog.data.routines.testing.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.seconds
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class RoutineViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator = FakeNavigator()

    private val converter: RoutineStateConverter = mockk()
    private val reducer: RoutineReducer = mockk()
    private val validator: RoutineValidator = mockk()
    private val routineUseCase: RoutineUseCase = mockk()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Before
    fun setup() {
        coEvery { converter.convert(state = any()) } returns RoutineViewState.Idle
        coEvery { routineUseCase.get(routineId = any()) } returns Routine.EMPTY
        every {
            reducer.setup(routine = any())
        } returns RoutineState.Data(routine = Routine.EMPTY, errors = emptyMap())
    }

    @Test
    fun `should get routine on load`() = coroutineRule {
        val routine = Routine.EMPTY
        coEvery { routineUseCase.get(routineId = any()) } returns routine
        every {
            reducer.setup(routine = any())
        } returns RoutineState.Data(routine = routine, errors = emptyMap())
        buildSut()

        coVerify {
            routineUseCase.get(routineId = 1.asID)
            reducer.setup(routine = routine)
        }
    }

    @Test
    fun `should update routine name onRoutineNameTextChange`() = coroutineRule {
        val state = RoutineState.Data(routine = Routine.EMPTY, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every { reducer.updateRoutineName(state = any(), text = any()) } returns state
        val sut = buildSut()

        sut.onRoutineNameTextChange(text = "name")

        verify { reducer.updateRoutineName(state = state, text = "name") }
    }

    @Test
    fun `should update routine startTimeOffset onRoutineStartTimeOffsetChange`() = coroutineRule {
        val state = RoutineState.Data(routine = Routine.EMPTY, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every {
            reducer.updateRoutineStartTimeOffset(
                state = any(),
                seconds = 10.seconds
            )
        } returns state
        val sut = buildSut()

        sut.onRoutineStartTimeOffsetChange(seconds = 10.seconds)

        verify { reducer.updateRoutineStartTimeOffset(state = state, seconds = 10.seconds) }
    }

    @Test
    fun `should navigate back onRoutineBack`() = coroutineRule {
        val sut = buildSut()

        sut.onRoutineBack()

        navigator.assertGoBack()
    }

    @Test
    fun `should apply errors when routine has errors onRoutineSave`() = coroutineRule {
        val errors: Map<RoutineField, RoutineFieldError> = mapOf(
            RoutineField.Name to RoutineFieldError.BlankRoutineName,
        )
        val state = RoutineState.Data(routine = Routine.EMPTY, errors = emptyMap())
        every { reducer.setup(routine = any()) } returns state
        every { reducer.applyRoutineErrors(state = any(), errors = any()) } returns state
        every { validator.validate(routine = any()) } returns errors
        val sut = buildSut()

        sut.onRoutineSave()

        verify {
            reducer.applyRoutineErrors(state = state, errors = errors)
            validator.validate(routine = Routine.EMPTY)
        }
        navigator.assertNoActions()
    }

    @Test
    @Ignore("Mockk doesn't fully support returning a value classes see https://github.com/mockk/mockk/issues/152")
    fun `should save and navigate to routineSummary when saving a new routine onRoutineSave`() =
        coroutineRule {
            val routine = Routine.EMPTY.copy(id = ID.new())
            val state = RoutineState.Data(routine = routine, errors = emptyMap())
            every { reducer.setup(routine = any()) } returns state
            every { validator.validate(routine = any()) } returns emptyMap()
            coEvery { routineUseCase.save(routine = any()) } returns 1.asID
            val sut = buildSut()

            sut.onRoutineSave()

            coVerify { validator.validate(routine = routine) }
            verify(exactly = 0) {
                reducer.applyRoutineErrors(state = any(), errors = any())
            }
            navigator.assertGoTo(
                route = RoutineSummaryRoute,
                input = RoutineSummaryRouteInput(routineId = 1.asID)
            )
        }

    @Test
    @Ignore("Mockk doesn't fully support returning a value classes see https://github.com/mockk/mockk/issues/152")
    fun `should save and navigate back when saving a routine onRoutineSave`() =
        coroutineRule {
            val routine = Routine.EMPTY.copy(id = 1.asID)
            val state = RoutineState.Data(routine = routine, errors = emptyMap())
            every { reducer.setup(routine = any()) } returns state
            every { validator.validate(routine = any()) } returns emptyMap()
            coEvery { routineUseCase.save(routine = any()) } returns 1.asID
            val sut = buildSut()

            sut.onRoutineSave()

            coVerify { validator.validate(routine = routine) }
            verify(exactly = 0) {
                reducer.applyRoutineErrors(state = any(), errors = any())
            }
            navigator.assertGoBack()
        }

    private fun buildSut(): RoutineViewModel {
        return RoutineViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = reducer,
            validator = validator,
            routineUseCase = routineUseCase
        )
    }
}
