package com.enricog.features.routines.detail.segment

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class SegmentViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val segment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Segment Name",
        time = 30.seconds,
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
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )

        val sut = buildSut()
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show error when load fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        store.enableErrorOnNextAccess()

        val sut = buildSut(store = store)
        advanceUntilIdle()

        sut.viewState.test { assertIs<SegmentViewState.Error>(awaitItem()) }
    }

    @Test
    fun `should reload when retry`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER)
            ),
            errors = immutableMapOf(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )
        store.enableErrorOnNextAccess()
        val sut = buildSut(store = store)
        advanceUntilIdle()

        sut.onRetryLoad()
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update segment name when name changes`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name Modified".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentNameTextChange(textFieldValue = "Segment Name Modified".toTextFieldValue())
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update segment time when time changes`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentTimeChange(text = "10".timeText)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update segment type when type changes`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = null,
                type = TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentTypeChange(timeType = TimeType.from(TimeTypeEntity.STOPWATCH))
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show errors when on save the segments has errors`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER),
            ),
            errors = immutableMapOf(SegmentField.Name to SegmentFieldError.BlankSegmentName),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()
        sut.onSegmentNameTextChange(textFieldValue = "".toTextFieldValue())
        advanceUntilIdle()

        sut.onSegmentSave()
        advanceUntilIdle()

        navigator.assertNoActions()
        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should save and go back when segment save succeeds`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = routine.copy(
            segments = listOf(segment.copy(name = "Segment Name Modified"))
        )
        val sut = buildSut(store = store)
        advanceUntilIdle()
        sut.onSegmentNameTextChange(textFieldValue = "Segment Name Modified".toTextFieldValue())
        advanceUntilIdle()

        sut.onSegmentSave()
        advanceUntilIdle()

        navigator.assertGoBack()
        assertEquals(expected, store.get().first())
    }

    @Test
    fun `should show message when save fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.from(TimeTypeEntity.TIMER)
            ),
            errors = emptyImmutableMap(),
            timeTypes = immutableListOf(
                TimeType.from(TimeTypeEntity.TIMER),
                TimeType.from(TimeTypeEntity.REST),
                TimeType.from(TimeTypeEntity.STOPWATCH)
            ),
            message = SegmentViewState.Data.Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        )
        val sut = buildSut(store = store)
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        sut.onSegmentSave()
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    private fun buildSut(store: FakeStore<List<Routine>> = FakeStore(listOf(routine))): SegmentViewModel {
        return SegmentViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = SegmentStateConverter(),
            reducer = SegmentReducer(),
            segmentUseCase = SegmentUseCase(
                routineDataSource = FakeRoutineDataSource(store)
            ),
            validator = SegmentValidator(),
            navigationActions = RoutinesNavigationActions(navigator)
        )
    }
}
