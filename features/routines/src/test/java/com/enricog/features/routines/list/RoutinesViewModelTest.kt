package com.enricog.features.routines.list

import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.RoutinesUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RoutinesViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val firstRoutine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "First Routine",
    )
    private val secondRoutine = Routine.EMPTY.copy(
        id = 2.asID,
        name = "Second Routine",
    )
    private val navigator = FakeNavigator()

    @Test
    fun `test on init should load all routines`() = coroutineRule {
        val expected = RoutinesViewState.Data(routines = listOf(firstRoutine, secondRoutine))
        val sut = buildSut()

        sut.viewState.test { assertEquals(expected, expectItem()) }
    }

    @Test
    fun `test on create routine button click should navigate to routine detail`() = coroutineRule {
        val sut = buildSut()

        sut.onCreateRoutineClick()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = ID.new())
        )
    }

    @Test
    fun `test on routine click should navigate to routine summary`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)
        val sut = buildSut()

        sut.onRoutineClick(routine)

        navigator.assertGoTo(
            route = RoutineSummaryRoute,
            input = RoutineSummaryRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `test on delete routine should remove it`() = coroutineRule {
        val expected = RoutinesViewState.Data(routines = listOf(secondRoutine))
        val sut = buildSut()

        sut.onRoutineDelete(firstRoutine)

        sut.viewState.test { assertEquals(expected, expectItem()) }
    }

    private fun buildSut(): RoutinesViewModel {
        return RoutinesViewModel(
            dispatchers = coroutineRule.dispatchers,
            converter = RoutinesStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutinesReducer(),
            routinesUseCase = RoutinesUseCase(
                routineDataSource = FakeRoutineDataSource(
                    store = FakeStore(listOf(firstRoutine, secondRoutine))
                )
            )
        )
    }
}
