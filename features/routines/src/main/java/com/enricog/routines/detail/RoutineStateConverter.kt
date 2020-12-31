package com.enricog.routines.detail

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.R
import com.enricog.routines.detail.models.*
import javax.inject.Inject

internal class RoutineStateConverter @Inject constructor() :
    StateConverter<RoutineState, RoutineViewState> {
    override suspend fun convert(state: RoutineState): RoutineViewState {
        return when (state) {
            RoutineState.Idle -> RoutineViewState.Idle
            is RoutineState.Data -> state.toViewState()
        }
    }

    private fun RoutineState.Data.toViewState(): RoutineViewState {
        return RoutineViewState.Data(
            routine = routine,
            editingSegment = editingSegment.asView,
            errors = errors.mapValues { it.value.stringResourceId }
        )
    }

    private val EditingSegment.asView: EditingSegmentView
        get() = when (this) {
            EditingSegment.None -> EditingSegmentView.None
            is EditingSegment.Data -> EditingSegmentView.Data(
                segment = segment,
                errors = errors.mapValues { it.value.stringResourceId },
                timeTypes = timeTypes
            )
        }

    private val ValidationError.stringResourceId: Int
        get() {
            return when (this) {
                ValidationError.BlankRoutineName -> R.string.field_error_message_routine_name_blank
                ValidationError.InvalidRoutineStartTimeOffset -> R.string.field_error_message_routine_start_time_offset_invalid
                ValidationError.NoSegmentsInRoutine -> R.string.field_error_message_routine_no_segments
                ValidationError.BlankSegmentName -> R.string.field_error_message_segment_name_blank
                ValidationError.InvalidSegmentTime -> R.string.field_error_message_segment_time_invalid
            }
        }
}