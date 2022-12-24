package com.enricog.features.routines.detail.routine

import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.asImmutableMap
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineFields
import com.enricog.features.routines.detail.routine.models.RoutineState
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action
import com.enricog.features.routines.detail.routine.models.RoutineState.Data.Action.SaveRoutineError
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.models.RoutineViewState.Data.Message
import javax.inject.Inject

internal class RoutineStateConverter @Inject constructor() :
    StateConverter<RoutineState, RoutineViewState> {

    override suspend fun convert(state: RoutineState): RoutineViewState {
        return when (state) {
            RoutineState.Idle -> RoutineViewState.Idle
            is RoutineState.Error -> RoutineViewState.Error(throwable = state.throwable)
            is RoutineState.Data -> state.toViewState()
        }
    }

    private fun RoutineState.Data.toViewState(): RoutineViewState {
        return RoutineViewState.Data(
            routine = RoutineFields(
                name = inputs.name,
                startTimeOffset = inputs.startTimeOffset
            ),
            errors = errors.asImmutableMap(),
            message = action?.toMessage()
        )
    }

    private fun Action.toMessage(): Message {
        return when (this) {
            SaveRoutineError -> Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        }
    }
}
