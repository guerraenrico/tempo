package com.enricog.routines.detail.segment

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.R
import com.enricog.routines.detail.segment.models.SegmentFieldError
import com.enricog.routines.detail.segment.models.SegmentState
import com.enricog.routines.detail.segment.models.SegmentViewState
import javax.inject.Inject

internal class SegmentStateConverter @Inject constructor() :
    StateConverter<SegmentState, SegmentViewState> {

    override suspend fun convert(state: SegmentState): SegmentViewState {
        return when (state) {
            SegmentState.Idle -> SegmentViewState.Idle
            is SegmentState.Data -> state.toViewState()
        }
    }

    private fun SegmentState.Data.toViewState(): SegmentViewState {
        return SegmentViewState.Data(
            segment = segment,
            errors = errors.mapValues { it.value.stringResourceId },
            timeTypes = timeTypes
        )
    }

    private val SegmentFieldError.stringResourceId: Int
        get() {
            return when (this) {
                SegmentFieldError.BlankSegmentName -> R.string.field_error_message_routine_name_blank
                SegmentFieldError.InvalidSegmentTime -> R.string.field_error_message_routine_start_time_offset_invalid
            }
        }

}