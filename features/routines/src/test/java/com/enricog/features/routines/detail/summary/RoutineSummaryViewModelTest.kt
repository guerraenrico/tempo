package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.RoutineSummaryUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RoutineSummaryViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val firstSegment = Segment.EMPTY.copy(
        id = 1.asID,
        name = "First Segment",
        rank = Rank.from("bbbbbb")
    )
    private val secondSegment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Second Segment",
        rank = Rank.from("cccccc")
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        segments = listOf(firstSegment, secondSegment)
    )

    private val navigator = FakeNavigator()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Test
    fun `should show data when load succeeds`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.SegmentItem(segment = secondSegment),
                RoutineSummaryItem.Space
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

        sut.viewState.test { assertIs<RoutineSummaryViewState.Error>(awaitItem()) }
    }

    @Test
    fun `should retry load when fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.SegmentItem(segment = secondSegment),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        store.enableErrorOnNextAccess()
        val sut = buildSut(store = store)
        advanceUntilIdle()

        sut.onRetryLoadClick()
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should goToSegment on add new segment`() = coroutineRule {
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentAdd()
        advanceUntilIdle()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = 1.asID, segmentId = ID.new())
        )
    }

    @Test
    fun `should goToSegment on segment selected`() = coroutineRule {
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentSelected(segment = secondSegment)
        advanceUntilIdle()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = 1.asID, segmentId = 2.asID)
        )
    }

    @Test
    fun `should update routine when segment is deleted`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentDelete(segment = secondSegment)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show message when delete segment fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.SegmentItem(segment = secondSegment),
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_error,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_error
            )
        )
        val sut = buildSut(store = store)
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        sut.onSegmentDelete(segment = secondSegment)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should retry delete segment when action is performed`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val sut = buildSut(store = store)
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        sut.onSegmentDelete(segment = secondSegment)
        advanceUntilIdle()

        sut.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should move segment when segment is moved`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = secondSegment.copy(rank = Rank.from("annnnn"))),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val sut = buildSut()
        advanceUntilIdle()

        sut.onSegmentMoved(segment = secondSegment, hoveredSegment = null)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show message when move segment fails`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.SegmentItem(segment = secondSegment),
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_move_error,
                actionTextResId = null
            )
        )
        val sut = buildSut(store = store)
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        sut.onSegmentMoved(segment = secondSegment, hoveredSegment = null)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should hide message when move segment fails and action is performed`() = coroutineRule {
        val store = FakeStore(listOf(routine))
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.SegmentItem(segment = secondSegment),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val sut = buildSut(store = store)
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        sut.onSegmentMoved(segment = secondSegment, hoveredSegment = null)
        advanceUntilIdle()

        sut.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should goToTimer when starting routine without errors`() = coroutineRule {
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineStart()

        navigator.assertGoTo(
            route = TimerRoute,
            input = TimerRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should show errors when starting routine with errors`() = coroutineRule {
        val routine = Routine.EMPTY.copy(
            id = 1.asID,
            name = "Routine Name",
            segments = emptyList()
        )
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val sut = buildSut(store = FakeStore(listOf(routine)))
        advanceUntilIdle()

        sut.onRoutineStart()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
        navigator.assertNoActions()
    }

    @Test
    fun `should goToRoutine on edit routine`() = coroutineRule {
        val sut = buildSut()
        advanceUntilIdle()

        sut.onRoutineEdit()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should goBack on back`() = coroutineRule {
        val sut = buildSut()
        advanceUntilIdle()

        sut.onBack()

        navigator.assertGoBack()
    }

    private fun buildSut(store: FakeStore<List<Routine>> = FakeStore(listOf(routine))): RoutineSummaryViewModel {
        return RoutineSummaryViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = RoutineSummaryStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutineSummaryReducer(),
            routineSummaryUseCase = RoutineSummaryUseCase(FakeRoutineDataSource(store)),
            moveSegmentUseCase = MoveSegmentUseCase(FakeRoutineDataSource(store)),
            validator = RoutineSummaryValidator()
        )
    }
}
