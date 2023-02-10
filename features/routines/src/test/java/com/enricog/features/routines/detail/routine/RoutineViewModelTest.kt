package com.enricog.features.routines.detail.routine

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.models.RoutineViewState.Data.Message
import com.enricog.features.routines.detail.routine.usecase.RoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.junit.Test

class RoutineViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        startTimeOffset = 30.seconds,
        segments = emptyList()
    )

    private val navigator = FakeNavigator()

    @Test
    fun `should show data when load succeeds`() = coroutineRule {
        val expectedViewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = null
        )
        val expectedFieldInputs = RoutineInputs(
            name = "Routine Name".toTextFieldValue(),
            startTimeOffset = "30".timeText
        )

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should show error when load fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        store.enableErrorOnNextAccess()

        val viewModel = buildViewModel(store = store)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(RoutineViewState.Error::class.java)
        }
    }

    @Test
    fun `should reload when retry`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expectedViewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = null
        )
        val expectedFieldInputs = RoutineInputs(
            name = "Routine Name".toTextFieldValue(),
            startTimeOffset = "30".timeText
        )
        store.enableErrorOnNextAccess()
        val viewModel = buildViewModel(store = store)
        advanceUntilIdle()

        viewModel.onRetryLoad()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update routine when name changes`() = coroutineRule {
        val expectedViewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = null
        )
        val expectedFieldInputs = RoutineInputs(
            name = "Routine Name Modified".toTextFieldValue(),
            startTimeOffset = "30".timeText
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineNameTextChange(textFieldValue = "Routine Name Modified".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update routine when start offset time changes`() = coroutineRule {
        val expectedViewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = null
        )
        val expectedFieldInputs = RoutineInputs(
            name = "Routine Name".toTextFieldValue(),
            startTimeOffset = "10".timeText
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineStartTimeOffsetChange(text = "10".timeText)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should not update routine when start offset time exceeds limit`() = coroutineRule {
        val expectedViewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = null
        )
        val expectedFieldInputs = RoutineInputs(
            name = "Routine Name".toTextFieldValue(),
            startTimeOffset = "30".timeText
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineStartTimeOffsetChange(text = "3000".timeText)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should navigate back when back`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onRoutineBack()

        navigator.assertGoBack()
    }

    @Test
    fun `should show errors when saving a routine with errors`() = coroutineRule {
        val expectedViewState = RoutineViewState.Data(
            errors = immutableMapOf(RoutineField.Name to RoutineFieldError.BlankRoutineName),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onRoutineNameTextChange(textFieldValue = "".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onRoutineSave()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        navigator.assertNoActions()
    }

    @Test
    fun `should save and navigate to routineSummary when saving a new routine`() = coroutineRule {
        val expectedViewState = Routine.EMPTY.copy(name = "New Routine Name")
        val store = FakeStore(emptyList<Routine>())
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 0L))
        val viewModel = buildViewModel(store = store, savedStateHandle = savedStateHandle)
        advanceUntilIdle()
        viewModel.onRoutineNameTextChange(textFieldValue = "New Routine Name".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onRoutineSave()
        advanceUntilIdle()

        store.get().first().let { actual ->
            assertThat(actual.name).isEqualTo(expectedViewState.name)
            assertThat(actual.segments).isEqualTo(expectedViewState.segments)
            assertThat(actual.startTimeOffset).isEqualTo(expectedViewState.startTimeOffset)

            navigator.assertGoTo(
                route = RoutineSummaryRoute,
                input = RoutineSummaryRouteInput(routineId = actual.id)
            )
        }
    }

    @Test
    fun `should save and navigate back when saving a existing routine`() = coroutineRule {
        val expectedViewState = routine.copy(name = "Routine Name Modified")
        val store = FakeStore(listOf(routine))
        val viewModel = buildViewModel(store)
        advanceUntilIdle()
        viewModel.onRoutineNameTextChange(textFieldValue = "Routine Name Modified".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onRoutineSave()
        advanceUntilIdle()

        assertThat(store.get().first()).isEqualTo(expectedViewState)
        navigator.assertGoBack()
    }

    @Test
    fun `should show message when save fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expectedViewState = RoutineViewState.Data(
            errors = emptyImmutableMap(),
            message = Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        )
        val viewModel = buildViewModel(store = store)
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        viewModel.onRoutineSave()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        navigator.assertNoActions()
    }

    private fun buildViewModel(
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
