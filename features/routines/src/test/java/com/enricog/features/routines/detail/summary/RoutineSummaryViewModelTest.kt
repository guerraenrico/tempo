package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField.Segments
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.features.routines.detail.summary.usecase.DeleteSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.DuplicateSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.GetRoutineUseCase
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.theme.TimeTypeColors
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RoutineSummaryViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val firstSegmentItem = RoutineSummaryItem.SegmentItem(
        id = 1.asID,
        name = "First Segment",
        time = 0.seconds,
        type = TimeType(
            nameStringResId = R.string.chip_time_type_timer_name,
            color = TimeTypeColors.TIMER,
            id = "TIMER"
        ),
        rank = "bbbbbb"
    )
    private val secondSegmentItem = RoutineSummaryItem.SegmentItem(
        id = 2.asID,
        name = "Second Segment",
        time = 0.seconds,
        type = TimeType(
            nameStringResId = R.string.chip_time_type_timer_name,
            color = TimeTypeColors.TIMER,
            id = "TIMER"
        ),
        rank = "cccccc"
    )
    private val thirdSegmentItem = RoutineSummaryItem.SegmentItem(
        id = 3.asID,
        name = "Third Segment",
        time = 0.seconds,
        type = TimeType(
            nameStringResId = R.string.chip_time_type_timer_name,
            color = TimeTypeColors.TIMER,
            id = "TIMER"
        ),
        rank = "dddddd"
    )
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
    private val thirdSegment = Segment.EMPTY.copy(
        id = 3.asID,
        name = "Third Segment",
        rank = Rank.from("dddddd")
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        segments = listOf(firstSegment, secondSegment, thirdSegment)
    )

    private val store = FakeStore(initialValue = listOf(routine))
    private val routineDataSource = FakeRoutineDataSource(store = store)
    private val navigator = FakeNavigator()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Test
    fun `should show data when load succeeds`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show error when load fails`() = coroutineRule {
        store.enableErrorOnNextAccess()

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test { assertIs<RoutineSummaryViewState.Error>(awaitItem()) }
    }

    @Test
    fun `should retry load when fails`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        store.enableErrorOnNextAccess()
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRetryLoad()
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should goToSegment on add new segment`() = coroutineRule {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentAdd()
        advanceUntilIdle()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = routine.id, segmentId = ID.new())
        )
    }

    @Test
    fun `should goToSegment on segment selected`() = coroutineRule {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentSelected(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = routine.id, segmentId = secondSegmentItem.id)
        )
    }

    @Test
    fun `should update routine and show message when segment is deleted`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_confirm,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_undo
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should restore segment when undo delete segment is clicked`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutine = routine
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
        assertEquals(expectedDatabaseRoutine, store.get().first())
    }

    @Test
    fun `should delete routine in database when snackbar is dismissed`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(firstSegment, thirdSegment)
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
        assertEquals(expectedDatabaseRoutine, store.get().first())
    }

    @Test
    fun `should show message when delete segment fails`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_error,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_error
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should delete routine in database when composable stops`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(firstSegment, thirdSegment)
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        viewModel.onStop()
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
        assertEquals(expectedDatabaseRoutine, store.get().first())
    }

    @Test
    fun `should retry delete segment when action is clicked`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should update routine when segment is duplicated`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                firstSegmentItem.copy(
                    id = ID.from(value = 4),
                    rank = "booooo"
                ),
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentDuplicate(segmentId = firstSegmentItem.id)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show message when duplicate segment fails`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_duplicate_error,
                actionTextResId = null
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        viewModel.onSegmentDuplicate(segmentId = firstSegmentItem.id)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should delete pending segment when another segment is duplicated`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                firstSegmentItem.copy(
                    id = 4.asID,
                    rank = "cccccc"
                ),
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(
                firstSegment,
                thirdSegment,
                firstSegment.copy(id = 4.asID, rank = Rank.from(value = "cccccc"))
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegmentItem.id)
        advanceUntilIdle()

        viewModel.onSegmentDuplicate(segmentId = firstSegmentItem.id)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
        assertEquals(expectedDatabaseRoutine, store.get().first())
    }

    @Test
    fun `should move segment when segment is moved`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                secondSegmentItem.copy(rank = "annnnn"),
                firstSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentMoved(draggedSegmentId = 2.asID, hoveredSegmentId = 1.asID)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should show message when move segment fails`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_move_error,
                actionTextResId = null
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        store.enableErrorOnNextAccess()
        viewModel.onSegmentMoved(
            draggedSegmentId = secondSegmentItem.id,
            hoveredSegmentId = firstSegmentItem.id
        )
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should hide message when move segment fails and action is performed`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                firstSegmentItem,
                secondSegmentItem,
                thirdSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        viewModel.onSegmentMoved(
            draggedSegmentId = secondSegmentItem.id,
            hoveredSegmentId = firstSegmentItem.id
        )
        advanceUntilIdle()

        viewModel.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should delete pending segment when another segment is moved`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                thirdSegmentItem.copy(rank = "annnnn"),
                firstSegmentItem,
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(
                firstSegment,
                thirdSegment.copy(rank = Rank.from(value = "annnnn"))
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.onSegmentMoved(
            draggedSegmentId = thirdSegmentItem.id,
            hoveredSegmentId = firstSegmentItem.id
        )
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
        assertEquals(expectedDatabaseRoutine, store.get().first())
    }

    @Test
    fun `should go to timer when starting routine without errors`() = coroutineRule {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineStart()
        advanceUntilIdle()

        navigator.assertGoTo(
            route = TimerRoute,
            input = TimerRouteInput(routineId = routine.id)
        )
    }

    @Test
    fun `should show errors when starting routine with errors`() = coroutineRule {
        store.update { listOf(routine.copy(segments = emptyList())) }
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineStart()
        advanceUntilIdle()

        viewModel.viewState.test { assertEquals(expected, awaitItem()) }
        navigator.assertNoActions()
    }

    @Test
    fun `should goToRoutine on edit routine`() = coroutineRule {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineEdit()
        runCurrent()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = routine.id)
        )
    }

    @Test
    fun `should goBack on back`() = coroutineRule {
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onBack()
        runCurrent()

        navigator.assertGoBack()
    }

    private fun buildViewModel(): RoutineSummaryViewModel {
        return RoutineSummaryViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.getDispatchers(),
            converter = RoutineSummaryStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutineSummaryReducer(),
            getRoutineUseCase = GetRoutineUseCase(routineDataSource),
            deleteSegmentUseCase = DeleteSegmentUseCase(routineDataSource),
            moveSegmentUseCase = MoveSegmentUseCase(routineDataSource),
            duplicateSegmentUseCase = DuplicateSegmentUseCase(routineDataSource),
            validator = RoutineSummaryValidator()
        )
    }
}
