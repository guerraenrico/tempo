package com.enricog.features.routines.detail.segment

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.FakeTimerThemeDataSource
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.usecase.GetTimerThemeUseCase
import com.enricog.features.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class SegmentViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val timerTheme = TimerTheme.DEFAULT
    private val segment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Segment Name",
        time = 30.seconds,
        rounds = 2,
        type = TimeTypeEntity.TIMER
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        segments = listOf(segment)
    )

    private val navigator = FakeNavigator()
    private val savedStateHandle = SavedStateHandle(
        mapOf("routineId" to 1L, "segmentId" to 2L)
    )

    @Test
    fun `should show data when load succeeds`() = coroutineRule {
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name".toTextFieldValue(),
            rounds = "2".toTextFieldValue(),
            time = "30".timeText,
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

        val viewModel = buildViewModel(routinesStore = store)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(SegmentViewState.Error::class.java)
        }
    }

    @Test
    fun `should reload when retry`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = immutableMapOf(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name".toTextFieldValue(),
            rounds = "2".toTextFieldValue(),
            time = "30".timeText,
        )
        store.enableErrorOnNextAccess()
        val viewModel = buildViewModel(routinesStore = store)
        advanceUntilIdle()

        viewModel.onRetryLoad()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update field input name when name changes`() = coroutineRule {
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name Modified".toTextFieldValue(),
            rounds = "2".toTextFieldValue(),
            time = "30".timeText,
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentNameChange(textFieldValue = "Segment Name Modified".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update field input rounds when rounds changes`() = coroutineRule {
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name".toTextFieldValue(),
            rounds = "3".toTextFieldValue(),
            time = "30".timeText,
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentRoundsChange(textFieldValue = "3".toTextFieldValue())
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update field input time when time changes`() = coroutineRule {
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name".toTextFieldValue(),
            rounds = "2".toTextFieldValue(),
            time = "10".timeText,
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentTimeChange(text = "10".timeText)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should not update segment time when time exceeds limit`() =
        coroutineRule {
            val expectedViewState = SegmentViewState.Data(
                isTimeFieldVisible = true,
                selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                errors = emptyImmutableMap(),
                timeTypeStyles = immutableListOf(
                    TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                    TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                    TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
                ),
                message = null
            )
            val expectedFieldInputs = SegmentInputs(
                name = "Segment Name".toTextFieldValue(),
                rounds = "2".toTextFieldValue(),
                time = "30".timeText,
            )
            val viewModel = buildViewModel()
            advanceUntilIdle()

            viewModel.onSegmentTimeChange(text = "50000".timeText)
            advanceUntilIdle()

            viewModel.viewState.test {
                assertThat(awaitItem()).isEqualTo(expectedViewState)
            }
            assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
        }

    @Test
    fun `should update segment type when type changes`() = coroutineRule {
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name".toTextFieldValue(),
            rounds = "2".toTextFieldValue(),
            time = "30".timeText,
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentTypeChange(
            timeTypeStyle = TimeTypeStyle.from(
                timeType = TimeTypeEntity.REST,
                timerTheme = timerTheme
            )
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should update segment type and reset time when type changes to stopwatch`() = coroutineRule {
        val expectedViewState = SegmentViewState.Data(
            isTimeFieldVisible = false,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val expectedFieldInputs = SegmentInputs(
            name = "Segment Name".toTextFieldValue(),
            rounds = "2".toTextFieldValue(),
            time = "".timeText,
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentTypeChange(
            timeTypeStyle = TimeTypeStyle.from(
                timeType = TimeTypeEntity.STOPWATCH,
                timerTheme = timerTheme
            )
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expectedViewState)
        }
        assertThat(viewModel.fieldInputs).isEqualTo(expectedFieldInputs)
    }

    @Test
    fun `should show errors when on save the segments has errors`() = coroutineRule {
        val expected = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = immutableMapOf(SegmentField.Name to SegmentFieldError.BlankSegmentName),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentNameChange(textFieldValue = "".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onSegmentSave()
        advanceUntilIdle()

        navigator.assertNoActions()
        viewModel.viewState.test { assertThat(awaitItem()).isEqualTo(expected) }
    }

    @Test
    fun `should save and go back when segment save succeeds`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = routine.copy(
            segments = listOf(segment.copy(name = "Segment Name Modified"))
        )
        val viewModel = buildViewModel(routinesStore = store)
        advanceUntilIdle()
        viewModel.onSegmentNameChange(textFieldValue = "Segment Name Modified".toTextFieldValue())
        advanceUntilIdle()

        viewModel.onSegmentSave()
        advanceUntilIdle()

        navigator.assertGoBack()
        assertThat(store.get().first()).isEqualTo(expected)
    }

    @Test
    fun `should show message when save fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = SegmentViewState.Data(
            isTimeFieldVisible = true,
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
            errors = emptyImmutableMap(),
            timeTypeStyles = immutableListOf(
                TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme),
                TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme)
            ),
            message = SegmentViewState.Data.Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        )
        val viewModel = buildViewModel(routinesStore = store)
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        viewModel.onSegmentSave()
        advanceUntilIdle()

        viewModel.viewState.test { assertThat(awaitItem()).isEqualTo(expected) }
    }

    private fun buildViewModel(routinesStore: FakeStore<List<Routine>> = FakeStore(listOf(routine))): SegmentViewModel {
        return SegmentViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = SegmentStateConverter(),
            reducer = SegmentReducer(),
            getTimerThemeUseCase = GetTimerThemeUseCase(
                timerThemeDataSource = FakeTimerThemeDataSource(
                    store = FakeStore(initialValue = listOf(timerTheme))
                )
            ),
            segmentUseCase = SegmentUseCase(
                routineDataSource = FakeRoutineDataSource(store = routinesStore)
            ),
            validator = SegmentValidator(),
            navigationActions = RoutinesNavigationActions(navigator)
        )
    }
}
