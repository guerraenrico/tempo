package com.enricog.routines.list

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Routine
import com.enricog.routines.list.models.RoutinesState
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.list.usecase.RoutinesUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoutinesViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val converter: RoutinesStateConverter = mockk()
    private val navigationActions: RoutinesNavigationActions = mockk(relaxUnitFun = true)
    private val reducer: RoutinesReducer = mockk()
    private val routinesUseCase: RoutinesUseCase = mockk(relaxUnitFun = true)

    @Before
    fun setup() {
        coEvery { converter.convert(any()) } returns RoutinesViewState.Idle
        coEvery { routinesUseCase.getAll() } returns emptyList()
        every { reducer.setup(any()) } returns RoutinesState.Empty
    }

    @Test
    fun `test on init should load all routines`() = coroutineRule {
        val routines = listOf(Routine.EMPTY)
        coEvery { routinesUseCase.getAll() } returns routines

        buildSut()
        advanceUntilIdle()

        coVerify {
            routinesUseCase.getAll()
            reducer.setup(routines)
        }
    }

    @Test
    fun `test onCreateRoutineClick should navigate to routine detail with routineId == null`() =
        coroutineRule {
            val sut = buildSut()
            advanceUntilIdle()

            sut.onCreateRoutineClick()

            verify { navigationActions.goToRoutine(routineId = null) }
        }

    @Test
    fun `test onRoutineClick should navigate to routine detail with routineId != null`() =
        coroutineRule {
            val routine = Routine.EMPTY.copy(id = 1)

            val sut = buildSut()
            advanceUntilIdle()

            sut.onRoutineClick(routine)

            verify { navigationActions.goToRoutine(routineId = 1) }
        }

    @Test
    fun `test onRoutineDelete should remove routine from state and from use case`() =
        coroutineRule {
            val routine = Routine.EMPTY
            val initialState = RoutinesState.Data(routines = listOf(routine))
            every { reducer.setup(any()) } returns initialState
            every { reducer.deleteRoutine(any(), any()) } returns RoutinesState.Empty

            val sut = buildSut()
            advanceUntilIdle()

            sut.onRoutineDelete(routine)

            coVerify {
                reducer.deleteRoutine(initialState, routine)
                routinesUseCase.delete(routine)
            }

        }


    private fun buildSut(): RoutinesViewModel {
        return RoutinesViewModel(
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            navigationActions = navigationActions,
            reducer = reducer,
            routinesUseCase = routinesUseCase
        )
    }

}