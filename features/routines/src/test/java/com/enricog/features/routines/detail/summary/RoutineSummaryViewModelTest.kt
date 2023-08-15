package com.enricog.features.routines.detail.summary

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.FakeRoutineStatisticsDataSource
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.FakeTimerThemeDataSource
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField.Segments
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo.SegmentsSummary
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemBuilder
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemRoutineInfo
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemSegmentItem
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemSegmentSectionTitle
import com.enricog.features.routines.detail.summary.test.RoutineSummaryViewStateBuilder
import com.enricog.features.routines.detail.summary.test.RoutineSummaryViewStateData
import com.enricog.features.routines.detail.summary.test.TIMER
import com.enricog.features.routines.detail.summary.usecase.DeleteSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.DuplicateSegmentUseCase
import com.enricog.features.routines.detail.summary.usecase.GetRoutineStatistic
import com.enricog.features.routines.detail.summary.usecase.GetRoutineUseCase
import com.enricog.features.routines.detail.summary.usecase.GetTimerThemeUseCase
import com.enricog.features.routines.detail.summary.usecase.MoveSegmentUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.features.routines.ui_components.goal_label.GoalLabel
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class RoutineSummaryViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val timerTheme = TimerTheme.DEFAULT
    private val firstSegment = Segment.EMPTY.copy(
        id = 1.asID,
        name = "First Segment",
        time = 10.seconds,
        rank = Rank.from("bbbbbb")
    )
    private val secondSegment = Segment.EMPTY.copy(
        id = 2.asID,
        name = "Second Segment",
        time = 10.seconds,
        rank = Rank.from("cccccc")
    )
    private val thirdSegment = Segment.EMPTY.copy(
        id = 3.asID,
        name = "Third Segment",
        time = 10.seconds,
        rank = Rank.from("dddddd")
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        segments = listOf(firstSegment, secondSegment, thirdSegment),
        frequencyGoal = FrequencyGoal.DEFAULT
    )
    private val statistic = Statistic(
        id = 1.asID,
        createdAt = OffsetDateTime.now(clock),
        routineId = routine.id,
        type = Statistic.Type.ROUTINE_COMPLETED,
        effectiveTime = 30.seconds
    )

    private val routinesStore = FakeStore(initialValue = listOf(routine))
    private val routineDataSource = FakeRoutineDataSource(store = routinesStore)
    private val timerThemeDataSource = FakeTimerThemeDataSource(store = FakeStore(listOf(timerTheme)))
    private val statisticsDataSource = FakeRoutineStatisticsDataSource(store = FakeStore(listOf(statistic)))
    private val navigator = FakeNavigator()
    private val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))

    @Test
    fun `should show data when load succeeds`() = coroutineRule {
        val expected = RoutineSummaryViewStateData { withFullItems() }

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should show error when load fails`() = coroutineRule {
        routinesStore.enableErrorOnNextAccess()

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(RoutineSummaryViewState.Error::class.java)
        }
    }

    @Test
    fun `should retry load when fails`() = coroutineRule {
        val expected = RoutineSummaryViewStateData { withFullItems() }
        routinesStore.enableErrorOnNextAccess()
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRetryLoad()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
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

        viewModel.onSegmentSelected(segmentId = secondSegment.id)
        advanceUntilIdle()

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(routineId = routine.id, segmentId = secondSegment.id)
        )
    }

    @Test
    fun `should update routine and show message when segment is deleted`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 2)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem { withPreset(num = 1) },
                RoutineSummaryItemSegmentItem { withPreset(num = 3) },
                RoutineSummaryItem.Space
            )
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_confirm,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_undo
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should restore segment when undo delete segment is clicked`() = coroutineRule {
        val expected = RoutineSummaryViewStateData { withFullItems() }
        val expectedDatabaseRoutine = routine
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(routinesStore.get().first()).isEqualTo(expectedDatabaseRoutine)
    }

    @Test
    fun `should delete routine in database when snackbar is dismissed`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 2)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem { withPreset(num = 1) },
                RoutineSummaryItemSegmentItem { withPreset(num = 3) },
                RoutineSummaryItem.Space
            )
        }
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(firstSegment, thirdSegment)
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(routinesStore.get().first()).isEqualTo(expectedDatabaseRoutine)
    }

    @Test
    fun `should show message when delete segment fails`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            withFullItems()
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_error,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_error
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()
        routinesStore.enableErrorOnNextAccess()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should delete routine in database when composable stops`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 2)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem { withPreset(num = 1) },
                RoutineSummaryItemSegmentItem { withPreset(num = 3) },
                RoutineSummaryItem.Space
            )
        }
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(firstSegment, thirdSegment)
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.onStop()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(routinesStore.get().first()).isEqualTo(expectedDatabaseRoutine)
    }

    @Test
    fun `should retry delete segment when action is clicked`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 2)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem { withPreset(num = 1) },
                RoutineSummaryItemSegmentItem { withPreset(num = 3) },
                RoutineSummaryItem.Space
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()
        routinesStore.enableErrorOnNextAccess()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should update routine when segment is duplicated`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "40".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 4)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem { withPreset(num = 1) },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 1)
                    id = ID.from(value = 4)
                    rank = "booooo"
                },
                RoutineSummaryItemSegmentItem { withPreset(num = 2) },
                RoutineSummaryItemSegmentItem { withPreset(num = 3) },
                RoutineSummaryItem.Space
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentDuplicate(segmentId = firstSegment.id)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should show message when duplicate segment fails`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            withFullItems()
            message = Message(
                textResId = R.string.label_routine_summary_segment_duplicate_error,
                actionTextResId = null
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        routinesStore.enableErrorOnNextAccess()
        viewModel.onSegmentDuplicate(segmentId = firstSegment.id)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should delete pending segment when another segment is duplicated`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "30".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 3)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 1)
                },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 1)
                    id = ID.from(value = 4)
                    rank = "cccccc"
                },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 3)
                },
                RoutineSummaryItem.Space
            )
        }
        val expectedDatabaseRoutine = routine.copy(
            segments = listOf(
                firstSegment,
                thirdSegment,
                firstSegment.copy(id = 4.asID, rank = Rank.from(value = "cccccc"))
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onSegmentDelete(segmentId = secondSegment.id)
        advanceUntilIdle()

        viewModel.onSegmentDuplicate(segmentId = firstSegment.id)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(routinesStore.get().first()).isEqualTo(expectedDatabaseRoutine)
    }

    @Test
    fun `should move segment when segment is moved`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo { withPreset() },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 2)
                    rank = "annnnn"
                },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 1)
                },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 3)
                },
                RoutineSummaryItem.Space
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onSegmentMoved(draggedSegmentId = 2.asID, hoveredSegmentId = 1.asID)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should show message when move segment fails`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            withFullItems()
            message = Message(
                textResId = R.string.label_routine_summary_segment_move_error,
                actionTextResId = null
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        routinesStore.enableErrorOnNextAccess()
        viewModel.onSegmentMoved(
            draggedSegmentId = secondSegment.id,
            hoveredSegmentId = firstSegment.id
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should hide message when move segment fails and action is performed`() = coroutineRule {
        val expected = RoutineSummaryViewStateData { withFullItems() }
        val viewModel = buildViewModel()
        advanceUntilIdle()
        routinesStore.enableErrorOnNextAccess()
        viewModel.onSegmentMoved(
            draggedSegmentId = secondSegment.id,
            hoveredSegmentId = firstSegment.id
        )
        advanceUntilIdle()

        viewModel.onSnackbarEvent(snackbarEvent = TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should delete pending segment when another segment is moved`() = coroutineRule {
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 2)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle { },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 3)
                    rank = "annnnn"
                },
                RoutineSummaryItemSegmentItem {
                    withPreset(num = 1)
                },
                RoutineSummaryItem.Space
            )
        }
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
            draggedSegmentId = thirdSegment.id,
            hoveredSegmentId = firstSegment.id
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(routinesStore.get().first()).isEqualTo(expectedDatabaseRoutine)
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
        routinesStore.update { listOf(routine.copy(segments = emptyList())) }
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    segmentsSummary = null
                },
                RoutineSummaryItemSegmentSectionTitle {
                    error = Segments to R.string.field_error_message_routine_no_segments
                },
                RoutineSummaryItem.Space
            )
        }
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineStart()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
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
            converter = RoutineSummaryStateConverter(clock = clock),
            navigationActions = RoutinesNavigationActions(navigator = navigator),
            reducer = RoutineSummaryReducer(),
            getTimerThemeUseCase = GetTimerThemeUseCase(timerThemeDataSource = timerThemeDataSource),
            getRoutineUseCase = GetRoutineUseCase(routineDataSource = routineDataSource),
            deleteSegmentUseCase = DeleteSegmentUseCase(routineDataSource = routineDataSource),
            moveSegmentUseCase = MoveSegmentUseCase(routineDataSource = routineDataSource),
            duplicateSegmentUseCase = DuplicateSegmentUseCase(routineDataSource = routineDataSource),
            getRoutineStatistic = GetRoutineStatistic(statisticsDataSource = statisticsDataSource, clock = clock),
            validator = RoutineSummaryValidator()
        )
    }

    private fun RoutineSummaryItemBuilder.SegmentItem.withPreset(num: Int) {
        val ids = listOf(1.asID, 2.asID, 3.asID)
        val names = listOf("First Segment", "Second Segment", "Third Segment")
        val ranks = listOf("bbbbbb", "cccccc", "dddddd")
        this.id = ids[num - 1]
        this.name = names[num - 1]
        this.rank = ranks[num - 1]
    }

    private fun RoutineSummaryItemBuilder.RoutineInfo.withPreset() {
        this.routineName = "Routine Name"
        this.goalLabel = GoalLabel(
            stringResId = R.string.label_routine_goal_text_day,
            formatArgs = immutableListOf(1, 1)
        )
        this.segmentsSummary = SegmentsSummary(
            estimatedTotalTime = "30".timeText,
            segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 3)
        )
    }

    private fun RoutineSummaryViewStateBuilder.Data.withFullItems() {
        items = immutableListOf(
            RoutineSummaryItemRoutineInfo { withPreset() },
            RoutineSummaryItemSegmentSectionTitle { },
            RoutineSummaryItemSegmentItem { withPreset(num = 1) },
            RoutineSummaryItemSegmentItem { withPreset(num = 2) },
            RoutineSummaryItemSegmentItem { withPreset(num = 3) },
            RoutineSummaryItem.Space
        )
    }
}
