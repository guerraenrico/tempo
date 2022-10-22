package com.enricog.features.routines.detail.routine

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineFields
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        startTimeOffset = 30.seconds,
        segments = emptyList()
    )
    private val routineFields = RoutineFields(
        name = "Routine Name".toTextFieldValue(),
        startTimeOffset = "30".timeText
    )

    private val navigator = FakeNavigator()

    @Test
    fun `should get routine on load`() = coroutineRule {
        val expected = RoutineViewState.Data(
            routine = routineFields,
            errors = emptyMap()
        )

        val sut = buildSut()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update routine when name change`() = coroutineRule {
        val expected = RoutineViewState.Data(
            routine = routineFields.copy(name = "Routine Name Modified".toTextFieldValue()),
            errors = emptyMap()
        )
        val sut = buildSut()

        sut.onRoutineNameTextChange(textFieldValue = "Routine Name Modified".toTextFieldValue())

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update routine when start offset time change`() = coroutineRule {
        val expected = RoutineViewState.Data(
            routine = routineFields.copy(startTimeOffset = "10".timeText),
            errors = emptyMap()
        )
        val sut = buildSut()

        sut.onRoutineStartTimeOffsetChange(text = "10".timeText)

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should navigate back when back`() = coroutineRule {
        val sut = buildSut()

        sut.onRoutineBack()

        navigator.assertGoBack()
    }

    @Test
    fun `should show errors when saving a routine with errors`() = coroutineRule {
        val expected = RoutineViewState.Data(
            routine = routineFields.copy(name = "".toTextFieldValue()),
            errors = mapOf(RoutineField.Name to RoutineFieldError.BlankRoutineName)
        )
        val sut = buildSut()
        sut.onRoutineNameTextChange(textFieldValue = "".toTextFieldValue())

        sut.onRoutineSave()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
        navigator.assertNoActions()
    }

    @Test
    fun `should save and navigate to routineSummary when saving a new routine`() = coroutineRule {
        val expected = Routine.EMPTY.copy(name = "New Routine Name")
        val store = FakeStore(emptyList<Routine>())
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 0L))
        val sut = buildSut(store = store, savedStateHandle = savedStateHandle)
        sut.onRoutineNameTextChange(textFieldValue = "New Routine Name".toTextFieldValue())

        sut.onRoutineSave()

        store.get().first().let { actual ->
            assertEquals(expected.name, actual.name)
            assertEquals(expected.segments, actual.segments)
            assertEquals(expected.startTimeOffset, actual.startTimeOffset)

            navigator.assertGoTo(
                route = RoutineSummaryRoute,
                input = RoutineSummaryRouteInput(routineId = actual.id)
            )
        }
    }

    @Test
    fun `should save and navigate back when saving a existing routine`() = coroutineRule {
        val expected = routine.copy(name = "Routine Name Modified")
        val store = FakeStore(listOf(routine))
        val sut = buildSut(store)
        sut.onRoutineNameTextChange(textFieldValue = "Routine Name Modified".toTextFieldValue())

        sut.onRoutineSave()

        assertEquals(expected, store.get().first())
        navigator.assertGoBack()
    }

    private fun buildSut(
        store: FakeStore<List<Routine>> = FakeStore(listOf(routine)),
        savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
    ): RoutineViewModel {
        return RoutineViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = RoutineStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutineReducer(),
            validator = RoutineValidator(),
            routineUseCase = RoutineUseCase(
                routineDataSource = FakeRoutineDataSource(store)
            )
        )
    }
}
