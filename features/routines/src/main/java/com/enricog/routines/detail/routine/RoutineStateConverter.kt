package com.enricog.routines.detail.routine

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.R
import com.enricog.routines.detail.routine.models.RoutineFieldError
import com.enricog.routines.detail.routine.models.RoutineState
import com.enricog.routines.detail.routine.models.RoutineViewState
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
            errors = errors.mapValues { it.value.stringResourceId }
        )
    }

    private val RoutineFieldError.stringResourceId: Int
        get() {
            return when (this) {
                RoutineFieldError.BlankRoutineName -> R.string.field_error_message_routine_name_blank
                RoutineFieldError.InvalidRoutineStartTimeOffset -> R.string.field_error_message_routine_start_time_offset_invalid
            }
        }
}