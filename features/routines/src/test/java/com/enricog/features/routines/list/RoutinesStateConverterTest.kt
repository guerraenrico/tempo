package com.enricog.features.routines.list

import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.R
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesItem.RoutineItem.SegmentsSummary
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.features.routines.ui_components.goal_label.GoalLabel
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import com.enricog.data.routines.api.entities.Routine.Companion as RoutineEntity
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class RoutinesStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val stateConverter = RoutinesStateConverter(clock = clock)

    @Test
    fun `should map idle state`() = coroutineRule {
        val state = RoutinesState.Idle
        val expected = RoutinesViewState.Idle

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map empty state`() = coroutineRule {
        val state = RoutinesState.Empty
        val expected = RoutinesViewState.Empty

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data state with segments summary null when routine has not segments`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            name = "Routine",
            segments = emptyList()
        )
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null,
            goalLabel = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = null,
            timerTheme = TimerTheme.DEFAULT,
            statistics = emptyList()
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data state with goal label when routine has statistics`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            id = 1.asID,
            name = "Routine",
            segments = emptyList(),
            frequencyGoal = FrequencyGoal(
                times = 2,
                period = FrequencyGoal.Period.DAY
            )
        )
        val routineItem = RoutinesItem.RoutineItem(
            id = 1.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null,
            goalLabel = GoalLabel(
                stringResId = R.string.label_routine_goal_text_day,
                formatArgs = immutableListOf(1, 2)
            )
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = null,
            timerTheme = TimerTheme.DEFAULT,
            statistics = listOf(
                Statistic.EMPTY.copy(
                    routineId = 1.asID,
                    createdAt = OffsetDateTime.now(clock)
                )
            )
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data state with segments summary when routine has at least one segment`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            name = "Routine",
            segments = listOf(
                Segment.EMPTY.copy(
                    name = "Segment 1",
                    time = 5.seconds,
                    type = TimeTypeEntity.TIMER
                ),
                Segment.EMPTY.copy(
                    name = "Segment 2",
                    time = 4.seconds,
                    type = TimeTypeEntity.REST
                ),
                Segment.EMPTY.copy(
                    name = "Segment 3",
                    time = 3.seconds,
                    type = TimeTypeEntity.TIMER
                ),
                Segment.EMPTY.copy(
                    name = "Segment 4",
                    time = 0.seconds,
                    type = TimeTypeEntity.STOPWATCH
                ),
            )
        )
        val timerTheme = TimerTheme.DEFAULT
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = SegmentsSummary(
                estimatedTotalTime = "12".timeText,
                segmentTypesCount = immutableMapOf(
                    TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme) to 2,
                    TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme) to 1,
                    TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme) to 1
                )
            ),
            goalLabel = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = null,
            timerTheme = timerTheme,
            statistics = emptyList()
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data state with segments summary with no expected total time when routine has only stopwatch segment`() =
        coroutineRule {
            val routineEntity = RoutineEntity.EMPTY.copy(
                name = "Routine",
                segments = listOf(
                    Segment.EMPTY.copy(
                        name = "Segment 1",
                        time = 0.seconds,
                        type = TimeTypeEntity.STOPWATCH
                    ),
                )
            )
            val timerTheme = TimerTheme.DEFAULT
            val routineItem = RoutinesItem.RoutineItem(
                id = 0.asID,
                name = "Routine",
                rank = "aaaaaa",
                segmentsSummary = SegmentsSummary(
                    estimatedTotalTime = null,
                    segmentTypesCount = immutableMapOf(
                        TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme) to 1
                    )
                ),
                goalLabel = null
            )
            val state = RoutinesState.Data(routines = listOf(routineEntity), action = null, timerTheme = timerTheme)
            val expected = RoutinesViewState.Data(
                routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
                message = null
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `test map data with delete routine error action`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            name = "Routine",
            segments = emptyList()
        )
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null,
            goalLabel = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DeleteRoutineError(routineEntity.id),
            timerTheme = TimerTheme.DEFAULT,
            statistics = emptyList()
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data with delete routine success action`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            name = "Routine",
            segments = emptyList()
        )
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null,
            goalLabel = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DeleteRoutineSuccess,
            timerTheme = TimerTheme.DEFAULT,
            statistics = emptyList()
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = Message(
                textResId = R.string.label_routines_delete_confirm,
                actionTextResId = R.string.action_text_routines_delete_undo
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data with move routine error action`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            name = "Routine",
            segments = emptyList()
        )
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null,
            goalLabel = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.MoveRoutineError,
            timerTheme = TimerTheme.DEFAULT,
            statistics = emptyList()
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = Message(
                textResId = R.string.label_routines_move_error,
                actionTextResId = null
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map data with duplicate routine error action`() = coroutineRule {
        val routineEntity = RoutineEntity.EMPTY.copy(
            name = "Routine",
            segments = emptyList()
        )
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null,
            goalLabel = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DuplicateRoutineError,
            timerTheme = TimerTheme.DEFAULT,
            statistics = emptyList()
        )
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
            message = Message(
                textResId = R.string.label_routines_duplicate_error,
                actionTextResId = null
            )
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map error`() = coroutineRule {
        val exception = Exception("something went wrong")
        val state = RoutinesState.Error(throwable = exception)
        val expected = RoutinesViewState.Error(throwable = exception)

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }
}
