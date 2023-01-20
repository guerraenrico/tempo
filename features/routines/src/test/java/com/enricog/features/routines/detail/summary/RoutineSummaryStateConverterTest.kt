package com.enricog.features.routines.detail.summary

import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.theme.TimeTypeColors
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineSummaryStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val sut = RoutineSummaryStateConverter()

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryViewState.Idle

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map data state without action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID),
                    Segment.EMPTY.copy(id = 2.asID),
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = null
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = null
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map data state with delete segment error action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID),
                    Segment.EMPTY.copy(id = 2.asID),
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DeleteSegmentError(segmentId = 1.asID)
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_error,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_error
            )
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map data state with delete segment success action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID),
                    Segment.EMPTY.copy(id = 2.asID),
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DeleteSegmentSuccess
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_delete_confirm,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_undo
            )
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map data state with move segment error action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID),
                    Segment.EMPTY.copy(id = 2.asID),
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.MoveSegmentError
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_move_error,
                actionTextResId = null
            )
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }

    @Test
    fun `test map data state with duplicate segment error action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID),
                    Segment.EMPTY.copy(id = 2.asID),
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DuplicateSegmentError
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "",
                    time = 0.seconds,
                    type = TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = Message(
                textResId = R.string.label_routine_summary_segment_duplicate_error,
                actionTextResId = null
            )
        )

        val result = sut.convert(state)

        assertEquals(expected, result)
    }
}
