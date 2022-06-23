package com.enricog.features.routines.detail.segment

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.timeText
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SegmentViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val segment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Segment Name",
        time = 30.seconds,
        type = TimeType.TIMER
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
    fun `should setup segment on load `() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.TIMER
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )

        val sut = buildSut()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update segment name onSegmentNameTextChange`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name Modified".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.TIMER
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val sut = buildSut()

        sut.onSegmentNameTextChange(textFieldValue = "Segment Name Modified".toTextFieldValue())

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update segment time onSegmentTimeChange`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "10".timeText,
                type = TimeType.TIMER
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val sut = buildSut()

        sut.onSegmentTimeChange(text = "10".timeText)

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update segment type onSegmentTypeChange`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "Segment Name".toTextFieldValue(),
                time = "".timeText,
                type = TimeType.STOPWATCH
            ),
            errors = emptyMap(),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val sut = buildSut()

        sut.onSegmentTypeChange(timeType = TimeType.STOPWATCH)

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show errors when segment has errors onSegmentConfirmed`() = coroutineRule {
        val expected = SegmentViewState.Data(
            segment = SegmentFields(
                name = "".toTextFieldValue(),
                time = "30".timeText,
                type = TimeType.TIMER
            ),
            errors = mapOf(SegmentField.Name to SegmentFieldError.BlankSegmentName),
            timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)
        )
        val sut = buildSut()
        sut.onSegmentNameTextChange(textFieldValue = "".toTextFieldValue())

        sut.onSegmentConfirmed()

        navigator.assertNoActions()
        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should save and go back when segment has no errors onSegmentConfirmed`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = routine.copy(
            segments = listOf(segment.copy(name = "Segment Name Modified"))
        )
        val sut = buildSut(store)
        sut.onSegmentNameTextChange(textFieldValue = "Segment Name Modified".toTextFieldValue())

        sut.onSegmentConfirmed()

        navigator.assertGoBack()
        assertEquals(expected, store.get().first())
    }

    private fun buildSut(store: FakeStore<List<Routine>> = FakeStore(listOf(routine))): SegmentViewModel {
        return SegmentViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
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
