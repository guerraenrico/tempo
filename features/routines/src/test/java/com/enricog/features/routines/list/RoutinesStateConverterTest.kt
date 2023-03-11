package com.enricog.features.routines.list

import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesItem.RoutineItem.SegmentsSummary
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.Routine.Companion as RoutineEntity
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class RoutinesStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val stateConverter = RoutinesStateConverter()

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
    fun `should map data state with segments summary null when routine has not segment`() =
        coroutineRule {
            val routineEntity = RoutineEntity.EMPTY.copy(
                name = "Routine",
                segments = emptyList()
            )
            val routineItem = RoutinesItem.RoutineItem(
                id = 0.asID,
                name = "Routine",
                rank = "aaaaaa",
                segmentsSummary = null
            )
            val state = RoutinesState.Data(routines = listOf(routineEntity), action = null)
            val expected = RoutinesViewState.Data(
                routinesItems = immutableListOf(routineItem, RoutinesItem.Space),
                message = null
            )

            val actual = stateConverter.convert(state)

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `should map data state with segments summary when routine has at least one segment`() =
        coroutineRule {
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
            val routineItem = RoutinesItem.RoutineItem(
                id = 0.asID,
                name = "Routine",
                rank = "aaaaaa",
                segmentsSummary = SegmentsSummary(
                    estimatedTotalTime = "12".timeText,
                    segmentTypesCount = immutableMapOf(
                        TimeType.from(TimeTypeEntity.TIMER) to 2,
                        TimeType.from(TimeTypeEntity.REST) to 1,
                        TimeType.from(TimeTypeEntity.STOPWATCH) to 1
                    )
                )
            )
            val state = RoutinesState.Data(routines = listOf(routineEntity), action = null)
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
            val routineItem = RoutinesItem.RoutineItem(
                id = 0.asID,
                name = "Routine",
                rank = "aaaaaa",
                segmentsSummary = SegmentsSummary(
                    estimatedTotalTime = null,
                    segmentTypesCount = immutableMapOf(
                        TimeType.from(TimeTypeEntity.STOPWATCH) to 1
                    )
                )
            )
            val state = RoutinesState.Data(routines = listOf(routineEntity), action = null)
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
            segmentsSummary = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DeleteRoutineError(routineEntity.id)
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
            segmentsSummary = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DeleteRoutineSuccess
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
            segmentsSummary = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.MoveRoutineError
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
            segmentsSummary = null
        )
        val state = RoutinesState.Data(
            routines = listOf(routineEntity),
            action = Action.DuplicateRoutineError
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
