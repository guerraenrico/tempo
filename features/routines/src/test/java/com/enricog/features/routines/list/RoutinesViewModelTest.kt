package com.enricog.features.routines.list

import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.RoutinesUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoutinesViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator = FakeNavigator()

    private val converter: RoutinesStateConverter = mockk()
    private val reducer: RoutinesReducer = mockk()
    private val routinesUseCase: RoutinesUseCase = mockk(relaxUnitFun = true)

    @Before
    fun setup() {
        coEvery { converter.convert(any()) } returns RoutinesViewState.Idle
        coEvery { routinesUseCase.getAll() } returns flowOf(emptyList())
        every { reducer.setup(any()) } returns RoutinesState.Empty
    }

    @Test
    fun `test on init should load all routines`() = coroutineRule {
        val routines = listOf(Routine.EMPTY)
        coEvery { routinesUseCase.getAll() } returns flowOf(routines)

        buildSut()

        coVerify {
            routinesUseCase.getAll()
            reducer.setup(routines)
        }
    }

    @Test
    fun `test onCreateRoutineClick should navigate to routine detail`() = coroutineRule {
        val sut = buildSut()

        sut.onCreateRoutineClick()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = ID.new())
        )
    }

    @Test
    fun `test onRoutineClick should navigate to routine summary`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)

        val sut = buildSut()

        sut.onRoutineClick(routine)

        navigator.assertGoTo(
            route = RoutineSummaryRoute,
            input = RoutineSummaryRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `test onRoutineDelete should remove routine from state and from use case`() =
        coroutineRule {
            val routine = Routine.EMPTY
            val initialState = RoutinesState.Data(routines = listOf(routine))
            every { reducer.setup(any()) } returns initialState

            val sut = buildSut()

            sut.onRoutineDelete(routine)

            coVerify { routinesUseCase.delete(routine) }
        }

    private fun buildSut(): RoutinesViewModel {
        return RoutinesViewModel(
            dispatchers = coroutineRule.dispatchers,
            converter = converter,
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = reducer,
            routinesUseCase = routinesUseCase
        )
    }
}
