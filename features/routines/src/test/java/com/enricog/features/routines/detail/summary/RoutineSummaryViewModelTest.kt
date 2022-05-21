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
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
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
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

class RoutineSummaryViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

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
    fun `should get routine on load`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.SegmentItem(segment = secondSegment),
                RoutineSummaryItem.Space
            )
        )
        val sut = buildSut()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should goToSegment on add new segment`() = coroutineRule {
        val sut = buildSut()

        sut.onSegmentAdd()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = 1.asID, segmentId = ID.new())
        )
    }

    @Test
    fun `should goToSegment on segment selected`() = coroutineRule {
        val sut = buildSut()

        sut.onSegmentSelected(segment = secondSegment)

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = 1.asID, segmentId = 2.asID)
        )
    }

    @Test
    fun `should udapte routine when segment is deleted`() = coroutineRule {
        val expected = RoutineSummaryViewState.Data(
            items = listOf(
                RoutineSummaryItem.RoutineInfo(routineName = "Routine Name"),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(segment = firstSegment),
                RoutineSummaryItem.Space
            )
        )
        val sut = buildSut()

        sut.onSegmentDelete(segment = secondSegment)

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
            )
        )
        val sut = buildSut()

        sut.onSegmentMoved(segment = secondSegment, hoveredSegment = null)

        sut.viewState.test { assertEquals(expected, awaitItem()) }
    }

    @Test
    fun `should goToTimer when starting routine without errors`() = coroutineRule {
        val sut = buildSut()

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
                RoutineSummaryItem.SegmentSectionTitle(error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments),
                RoutineSummaryItem.Space
            )
        )
        val sut = buildSut(store = FakeStore(listOf(routine)))

        sut.onRoutineStart()

        sut.viewState.test { assertEquals(expected, awaitItem()) }
        navigator.assertNoActions()
    }

    @Test
    fun `should goToRoutine on edit routine`() = coroutineRule {
        val sut = buildSut()

        sut.onRoutineEdit()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should goBack on back`() = coroutineRule {
        val sut = buildSut()

        sut.onBack()

        navigator.assertGoBack()
    }

    private fun buildSut(store: FakeStore<List<Routine>> = FakeStore(listOf(routine))): RoutineSummaryViewModel {
        return RoutineSummaryViewModel(
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.dispatchers,
            converter = RoutineSummaryStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutineSummaryReducer(),
            routineSummaryUseCase = RoutineSummaryUseCase(FakeRoutineDataSource(store)),
            moveSegmentUseCase = MoveSegmentUseCase(FakeRoutineDataSource(store)),
            validator = RoutineSummaryValidator()
        )
    }
}
