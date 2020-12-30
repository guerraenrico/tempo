package com.enricog.routines.detail

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.R
import com.enricog.routines.detail.models.RoutineState
import com.enricog.routines.detail.models.RoutineViewState
import com.enricog.routines.detail.models.ValidationError
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
            editingSegment = editingSegment,
            errors = errors.mapValues { it.value.stringResourceId }
        )
    }

    private val ValidationError.stringResourceId: Int
        get() {
            return when (this) {
                ValidationError.BlankRoutineName -> R.string.field_error_message_routine_name_blank
                ValidationError.BlankSegmentName -> R.string.field_error_message_segment_name_blank
            }
        }
}