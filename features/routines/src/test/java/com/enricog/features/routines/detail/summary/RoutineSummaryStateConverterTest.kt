package com.enricog.features.routines.detail.summary

import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo.SegmentsSummary
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemBuilder
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemRoutineInfo
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemSegmentItem
import com.enricog.features.routines.detail.summary.test.RoutineSummaryItemSegmentSectionTitle
import com.enricog.features.routines.detail.summary.test.RoutineSummaryStateBuilder
import com.enricog.features.routines.detail.summary.test.RoutineSummaryStateData
import com.enricog.features.routines.detail.summary.test.RoutineSummaryViewStateBuilder
import com.enricog.features.routines.detail.summary.test.RoutineSummaryViewStateData
import com.enricog.features.routines.detail.summary.test.STOPWATCH
import com.enricog.features.routines.detail.summary.test.TIMER
import com.enricog.features.routines.ui_components.goal_label.GoalLabel
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class RoutineSummaryStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))
    private val stateConverter = RoutineSummaryStateConverter(clock = clock)

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryViewState.Idle

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state without action`() = coroutineRule {
        val state = RoutineSummaryStateData { withPreset() }
        val expected = RoutineSummaryViewStateData { withPreset() }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with goal`() = coroutineRule {
        val state = RoutineSummaryStateData {
            withPreset()
            routine = routine.copy(
                frequencyGoal = FrequencyGoal(times = 2, period = FrequencyGoal.Period.DAY)
            )
            statistics = listOf(Statistic.EMPTY.copy(createdAt = OffsetDateTime.now(clock)))
        }
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    withPreset()
                    goalLabel = GoalLabel(
                        stringResId = R.string.label_routine_goal_text_day,
                        formatArgs = immutableListOf(1, 2)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle {
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                },
                RoutineSummaryItemSegmentItem { withPreset(num = 1) },
                RoutineSummaryItemSegmentItem { withPreset(num = 2) },
                RoutineSummaryItem.Space
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with delete segment error action`() = coroutineRule {
        val state = RoutineSummaryStateData {
            withPreset()
            action = Action.DeleteSegmentError(segmentId = 1.asID)
        }
        val expected = RoutineSummaryViewStateData {
            withPreset()
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_error,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_error
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with delete segment success action`() = coroutineRule {
        val state = RoutineSummaryStateData {
            withPreset()
            action = Action.DeleteSegmentSuccess
        }
        val expected = RoutineSummaryViewStateData {
            withPreset()
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_confirm,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_undo
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with move segment error action`() = coroutineRule {
        val state = RoutineSummaryStateData {
            withPreset()
            action = Action.MoveSegmentError
        }
        val expected = RoutineSummaryViewStateData {
            withPreset()
            message = Message(
                textResId = R.string.label_routine_summary_segment_move_error,
                actionTextResId = null
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with duplicate segment error action`() = coroutineRule {
        val state = RoutineSummaryStateData {
            withPreset()
            action = Action.DuplicateSegmentError
        }
        val expected = RoutineSummaryViewStateData {
            withPreset()
            message = Message(
                textResId = R.string.label_routine_summary_segment_duplicate_error,
                actionTextResId = null
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with no segment summery when routine has no segments`() = coroutineRule {
        val state = RoutineSummaryStateData {
            routine = Routine.EMPTY.copy(name = "routineName", segments = emptyList())
        }
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo { routineName = "routineName" },
                RoutineSummaryItemSegmentSectionTitle {},
                RoutineSummaryItem.Space
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with expected total time when routine has only stopwatch segment`() = coroutineRule {
        val state = RoutineSummaryStateData {
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", type = TimeTypeEntity.STOPWATCH)
                )
            )
        }
        val expected = RoutineSummaryViewStateData {
            items = immutableListOf(
                RoutineSummaryItemRoutineInfo {
                    routineName = "routineName"
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = null,
                        segmentTypesCount = immutableMapOf(TimeTypeStyle.STOPWATCH to 1)
                    )
                },
                RoutineSummaryItemSegmentSectionTitle {},
                RoutineSummaryItemSegmentItem {
                    id = 1.asID
                    name = "Segment 1"
                    time = "0".timeText
                    type = TimeTypeStyle.STOPWATCH
                    rank = "aaaaaa"
                },
                RoutineSummaryItem.Space
            )
        }

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    private fun RoutineSummaryStateBuilder.Data.withPreset() {
        routine = Routine.EMPTY.copy(
            name = "Routine Name",
            segments = listOf(
                Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", time = 10.seconds, rank = Rank.from("bbbbbb")),
                Segment.EMPTY.copy(id = 2.asID, name = "Segment 2", time = 10.seconds, rank = Rank.from("cccccc"))
            )
        )
        errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments)
    }

    private fun RoutineSummaryItemBuilder.SegmentItem.withPreset(num: Int) {
        val ids = listOf(1.asID, 2.asID)
        val names = listOf("Segment 1", "Segment 2")
        val ranks = listOf("bbbbbb", "cccccc")
        this.id = ids[num - 1]
        this.name = names[num - 1]
        this.rank = ranks[num - 1]
    }

    private fun RoutineSummaryItemBuilder.RoutineInfo.withPreset() {
        this.routineName = "Routine Name"
        this.segmentsSummary = SegmentsSummary(
            estimatedTotalTime = "20".timeText,
            segmentTypesCount = immutableMapOf(TimeTypeStyle.TIMER to 2)
        )
    }

    private fun RoutineSummaryViewStateBuilder.Data.withPreset() {
        items = immutableListOf(
            RoutineSummaryItemRoutineInfo { withPreset() },
            RoutineSummaryItemSegmentSectionTitle {
                error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
            },
            RoutineSummaryItemSegmentItem { withPreset(num = 1) },
            RoutineSummaryItemSegmentItem { withPreset(num = 2) },
            RoutineSummaryItem.Space
        )
    }
}
