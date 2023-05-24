package com.enricog.features.routines.detail.summary

import androidx.compose.ui.graphics.Color
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo.SegmentsSummary
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.timeText
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class RoutineSummaryStateConverterTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val stateConverter = RoutineSummaryStateConverter()

    @Test
    fun `test map idle state`() = coroutineRule {
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryViewState.Idle

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state without action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", time = 10.seconds),
                    Segment.EMPTY.copy(id = 2.asID, name = "Segment 2", time = 10.seconds)
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle(
                                nameStringResId = R.string.chip_time_type_timer_name,
                                backgroundColor = Color.Red,
                                onBackgroundColor = Color.White,
                                id = "TIMER"
                            ) to 2
                        )
                    )
                ),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "Segment 1",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "Segment 2",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with delete segment error action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", time = 10.seconds),
                    Segment.EMPTY.copy(id = 2.asID, name = "Segment 2", time = 10.seconds)
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DeleteSegmentError(segmentId = 1.asID),
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle(
                                nameStringResId = R.string.chip_time_type_timer_name,
                                backgroundColor = Color.Red,
                                onBackgroundColor = Color.White,
                                id = "TIMER"
                            ) to 2
                        )
                    )
                ),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "Segment 1",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "Segment 2",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
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

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with delete segment success action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", time = 10.seconds),
                    Segment.EMPTY.copy(id = 2.asID, name = "Segment 2", time = 10.seconds)
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DeleteSegmentSuccess,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle(
                                nameStringResId = R.string.chip_time_type_timer_name,
                                backgroundColor = Color.Red,
                                onBackgroundColor = Color.White,
                                id = "TIMER"
                            ) to 2
                        )
                    )
                ),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "Segment 1",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "Segment 2",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
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

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with move segment error action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", time = 10.seconds),
                    Segment.EMPTY.copy(id = 2.asID, name = "Segment 2", time = 10.seconds)
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.MoveSegmentError,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle(
                                nameStringResId = R.string.chip_time_type_timer_name,
                                backgroundColor = Color.Red,
                                onBackgroundColor = Color.White,
                                id = "TIMER"
                            ) to 2
                        )
                    )
                ),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "Segment 1",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "Segment 2",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
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

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with duplicate segment error action`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", time = 10.seconds),
                    Segment.EMPTY.copy(id = 2.asID, name = "Segment 2", time = 10.seconds)
                )
            ),
            errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments),
            action = Action.DuplicateSegmentError,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = "20".timeText,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle(
                                nameStringResId = R.string.chip_time_type_timer_name,
                                backgroundColor = Color.Red,
                                onBackgroundColor = Color.White,
                                id = "TIMER"
                            ) to 2
                        )
                    )
                ),
                RoutineSummaryItem.SegmentSectionTitle(
                    error = RoutineSummaryField.Segments to R.string.field_error_message_routine_no_segments
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "Segment 1",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.SegmentItem(
                    id = 2.asID,
                    name = "Segment 2",
                    time = "10".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
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

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with no segment summery when routine has no segments`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = emptyList()
            ),
            errors = emptyMap(),
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = null
                ),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.Space
            ),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test map data state with expected total time when routine has only stopwatch segment`() = coroutineRule {
        val state = RoutineSummaryState.Data(
            routine = Routine.EMPTY.copy(
                name = "routineName",
                segments = listOf(
                    Segment.EMPTY.copy(id = 1.asID, name = "Segment 1", type = TimeTypeEntity.STOPWATCH)
                )
            ),
            errors = emptyMap(),
            action = null,
            timerTheme = TimerTheme.DEFAULT
        )
        val expected = RoutineSummaryViewState.Data(
            items = immutableListOf(
                RoutineSummaryItem.RoutineInfo(
                    routineName = "routineName",
                    segmentsSummary = SegmentsSummary(
                        estimatedTotalTime = null,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle(
                                nameStringResId = R.string.chip_time_type_stopwatch_name,
                                backgroundColor = Color.Black,
                                onBackgroundColor = Color.White,
                                id = "STOPWATCH"
                            ) to 1
                        )
                    )
                ),
                RoutineSummaryItem.SegmentSectionTitle(error = null),
                RoutineSummaryItem.SegmentItem(
                    id = 1.asID,
                    name = "Segment 1",
                    time = "0".timeText,
                    type = TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_stopwatch_name,
                        backgroundColor = Color.Black,
                        onBackgroundColor = Color.White,
                        id = "STOPWATCH"
                    ),
                    rank = "aaaaaa"
                ),
                RoutineSummaryItem.Space
            ),
            message = null
        )

        val actual = stateConverter.convert(state)

        assertThat(actual).isEqualTo(expected)
    }
}
