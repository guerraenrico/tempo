package com.enricog.features.routines.list

import app.cash.turbine.test
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.DeleteRoutineUseCase
import com.enricog.features.routines.list.usecase.DuplicateRoutineUseCase
import com.enricog.features.routines.list.usecase.GetRoutinesUseCase
import com.enricog.features.routines.list.usecase.MoveRoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import com.enricog.data.routines.api.entities.Routine as RoutineEntity

class RoutinesViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val firstRoutineEntity = RoutineEntity.EMPTY.copy(
        id = 1.asID,
        name = "First Routine",
        rank = Rank.from(value = "aaaaaa")
    )
    private val secondRoutineEntity = RoutineEntity.EMPTY.copy(
        id = 2.asID,
        name = "Second Routine",
        rank = Rank.from(value = "bbbbbb")
    )
    private val firstRoutine = RoutinesItem.RoutineItem(
        id = 1.asID,
        name = "First Routine",
        rank = "aaaaaa",
        segmentsSummary = null
    )
    private val secondRoutine = RoutinesItem.RoutineItem(
        id = 2.asID,
        name = "Second Routine",
        rank = "bbbbbb",
        segmentsSummary = null
    )
    private val navigator = FakeNavigator()
    private val store = FakeStore(listOf(firstRoutineEntity, secondRoutineEntity))
    private val routineDataSource = FakeRoutineDataSource(store = store)

    @Test
    fun `should should show data when load succeeds`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(firstRoutine, secondRoutine, RoutinesItem.Space),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show error when load fails`() = coroutineRule {
        store.enableErrorOnNextAccess()

        val sut = buildSut()
        advanceUntilIdle()

        sut.viewState.test { assertIs<RoutinesViewState.Error>(awaitItem()) }
    }

    @Test
    fun `should navigate to routine detail when create routine button clicked`() = coroutineRule {
        val sut = buildSut()

        sut.onCreateRoutine()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = ID.new())
        )
    }

    @Test
    fun `should navigate to routine summary when routine clicked`() = coroutineRule {
        val routineId = 1.asID
        val sut = buildSut()

        sut.onRoutine(routineId = routineId)

        navigator.assertGoTo(
            route = RoutineSummaryRoute,
            input = RoutineSummaryRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should remove routine when delete routine clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, RoutinesItem.Space),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show message when delete routine clicked and deletion fails`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(firstRoutine, secondRoutine, RoutinesItem.Space),
            message = RoutinesViewState.Data.Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )
        )
        val sut = buildSut()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()

        sut.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should duplicate routine when duplicate routine clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                firstRoutine.copy(
                    id = ID.from(value = 3),
                    rank = "annnnn"
                ),
                secondRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineDuplicate(routineId = firstRoutine.id)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show message when duplicate routine clicked and duplication fails`() =
        coroutineRule {
            val expected = RoutinesViewState.Data(
                routinesItems = immutableListOf(firstRoutine, secondRoutine, RoutinesItem.Space),
                message = RoutinesViewState.Data.Message(
                    textResId = R.string.label_routines_duplicate_error,
                    actionTextResId = null
                )
            )
            val sut = buildSut()
            advanceUntilIdle()
            store.enableErrorOnNextAccess()

            sut.onRoutineDuplicate(routineId = firstRoutine.id)
            advanceUntilIdle()

            sut.viewState.test { assertEquals(expected, awaitItem()) }
        }

    @Test
    fun `should hide message when when snackbar is dismissed`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(firstRoutine, secondRoutine, RoutinesItem.Space),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        sut.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        sut.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should retry delete when when snackbar action is clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, RoutinesItem.Space),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        sut.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        sut.onSnackbarEvent(TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should reload when retry button clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, RoutinesItem.Space),
            message = null
        )
        val sut = buildSut()
        store.update { routines -> routines.filter { it.id == secondRoutine.id } }

        sut.onRetryLoad()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    private fun buildSut(): RoutinesViewModel {
        return RoutinesViewModel(
            dispatchers = coroutineRule.getDispatchers(),
            converter = RoutinesStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutinesReducer(),
            getRoutinesUseCase = GetRoutinesUseCase(
                routineDataSource = routineDataSource
            ),
            deleteRoutineUseCase = DeleteRoutineUseCase(
                routineDataSource = routineDataSource
            ),
            moveRoutineUseCase = MoveRoutineUseCase(
                routineDataSource = routineDataSource
            ),
            duplicateRoutineUseCase = DuplicateRoutineUseCase(
                routineDataSource = routineDataSource
            )
        )
    }
}
