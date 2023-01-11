package com.enricog.features.routines.detail.summary

import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.asImmutableList
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DeleteSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.MoveSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action.DuplicateSegmentError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import javax.inject.Inject

internal class RoutineSummaryStateConverter @Inject constructor() :
    StateConverter<RoutineSummaryState, RoutineSummaryViewState> {

    override suspend fun convert(state: RoutineSummaryState): RoutineSummaryViewState {
        return when (state) {
            RoutineSummaryState.Idle -> RoutineSummaryViewState.Idle
            is RoutineSummaryState.Data -> state.toViewState()
            is RoutineSummaryState.Error -> RoutineSummaryViewState.Error(throwable = state.throwable)
        }
    }

    private fun RoutineSummaryState.Data.toViewState(): RoutineSummaryViewState.Data {
        val items: List<RoutineSummaryItem> = buildList {
            add(RoutineSummaryItem.RoutineInfo(routineName = routine.name))

            val mappedErrors = errors.mapValues { it.value.stringResourceId }
            add(
                RoutineSummaryItem.SegmentSectionTitle(
                    error = mappedErrors[RoutineSummaryField.Segments]?.let {
                        RoutineSummaryField.Segments to it
                    }
                )
            )

            addAll(
                routine.segments.map {
                    RoutineSummaryItem.SegmentItem.from(segment = it)
                }
            )
            add(RoutineSummaryItem.Space)
        }
        return RoutineSummaryViewState.Data(
            items = items.asImmutableList(),
            message = action?.toMessage()
        )
    }

    private val RoutineSummaryFieldError.stringResourceId: Int
        get() = when (this) {
            RoutineSummaryFieldError.NoSegments -> R.string.field_error_message_routine_no_segments
        }

    private fun Action.toMessage(): Message {
        return when (this) {
            is DeleteSegmentError -> Message(
                textResId = R.string.label_routine_summary_segment_delete_error,
                actionTextResId = R.string.action_text_routine_summary_segment_delete_error
            )
            MoveSegmentError -> Message(
                textResId = R.string.label_routine_summary_segment_move_error,
                actionTextResId = null
            )
            DuplicateSegmentError -> Message(
                textResId = R.string.label_routine_summary_segment_duplicate_error,
                actionTextResId = null
            )
        }
    }
}
