package com.enricog.features.routines.detail.routine

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.models.RoutineViewState.Data.Message
import com.enricog.features.routines.detail.routine.test.RoutineInputs
import com.enricog.features.routines.detail.routine.test.RoutineViewStateData
import com.enricog.features.routines.detail.routine.test.frequencyGoalDropDownItems
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
        rounds = 2,
        preparationTime = 30.seconds,
        segments = emptyList(),
        frequencyGoal = null
    )

    private val navigator = FakeNavigator()

    @Test
    fun `should show data when load succeeds`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs { from(routine) }

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
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs { from(routine) }
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
    fun `should update fields input when name changes`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            name = "Routine Name Modified".toTextFieldValue()
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineNameChange(textFieldValue = "Routine Name Modified".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update fields input when rounds changes`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            rounds = "3".toTextFieldValue()
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineRoundsChange(textFieldValue = "3".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update fields input when preparation time changes`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            preparationTime = "10".timeText
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutinePreparationTimeChange(text = "10".timeText)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update fields input with default goal when frequency goal is checked`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            frequencyGoal = RoutineInputs.FrequencyGoalInput.Value(
                frequencyGoalTimes = "1".toTextFieldValue(),
                frequencyGoalPeriod = frequencyGoalDropDownItems.getValue(FrequencyGoal.Period.DAY)
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onFrequencyGoalCheck(isChecked = true)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update fields input when frequency goal times changes`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            frequencyGoal = RoutineInputs.FrequencyGoalInput.Value(
                frequencyGoalTimes = "2".toTextFieldValue(),
                frequencyGoalPeriod = frequencyGoalDropDownItems.getValue(FrequencyGoal.Period.DAY)
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onFrequencyGoalCheck(isChecked = true)
        advanceUntilIdle()

        viewModel.onFrequencyGoalTimesChange(textFieldValue = "2".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should not update fields input when frequency goal times changes and frequency is not checked`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            frequencyGoal = RoutineInputs.FrequencyGoalInput.None
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onFrequencyGoalTimesChange(textFieldValue = "2".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update fields input when frequency goal period changes`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            frequencyGoal = RoutineInputs.FrequencyGoalInput.Value(
                frequencyGoalTimes = "1".toTextFieldValue(),
                frequencyGoalPeriod = frequencyGoalDropDownItems.getValue(FrequencyGoal.Period.WEEK)
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onFrequencyGoalCheck(isChecked = true)
        advanceUntilIdle()

        viewModel.onFrequencyGoalPeriodChange(
            dropDownItem = frequencyGoalDropDownItems.getValue(FrequencyGoal.Period.WEEK)
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should not update fields input when frequency goal periods changes and frequency is not checked`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs {
            from(routine)
            frequencyGoal = RoutineInputs.FrequencyGoalInput.None
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onFrequencyGoalPeriodChange(
            dropDownItem = frequencyGoalDropDownItems.getValue(FrequencyGoal.Period.WEEK)
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should not update routine when start offset time exceeds limit`() = coroutineRule {
        val expectedViewState = RoutineViewStateData {}
        val expectedFieldInputs = RoutineInputs { from(routine) }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutinePreparationTimeChange(text = "3000".timeText)
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
        val expectedViewState = RoutineViewStateData {
            errors = immutableMapOf(RoutineField.Name to RoutineFieldError.BlankRoutineName)
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onRoutineNameChange(textFieldValue = "".toTextFieldValue())
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
        val expectedStoredRoutine = Routine.EMPTY.copy(name = "New Routine Name")
        val store = FakeStore(emptyList<Routine>())
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 0L))
        val viewModel = buildViewModel(store = store, savedStateHandle = savedStateHandle)
        advanceUntilIdle()
        viewModel.onRoutineNameChange(textFieldValue = "New Routine Name".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onRoutineSave()
        advanceUntilIdle()

        store.get().first().let { actual ->
            assertThat(actual.name).isEqualTo(expectedStoredRoutine.name)
            assertThat(actual.segments).isEqualTo(expectedStoredRoutine.segments)
            assertThat(actual.preparationTime).isEqualTo(expectedStoredRoutine.preparationTime)

            navigator.assertGoTo(
                route = RoutineSummaryRoute,
                input = RoutineSummaryRouteInput(routineId = actual.id)
            )
        }
    }

    @Test
    fun `should save and navigate back when saving a existing routine`() = coroutineRule {
        val expectedStoredRoutine = routine.copy(name = "Routine Name Modified")
        val store = FakeStore(listOf(routine))
        val viewModel = buildViewModel(store)
        advanceUntilIdle()
        viewModel.onRoutineNameChange(textFieldValue = "Routine Name Modified".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onRoutineSave()
        advanceUntilIdle()

        assertThat(store.get().first()).isEqualTo(expectedStoredRoutine)
        navigator.assertGoBack()
    }

    @Test
    fun `should show message when save fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expectedViewState = RoutineViewStateData {
            message = Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        }
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
