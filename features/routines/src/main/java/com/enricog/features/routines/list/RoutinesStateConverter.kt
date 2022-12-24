package com.enricog.features.routines.list

import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.asImmutableList
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Routine
import javax.inject.Inject

internal class RoutinesStateConverter @Inject constructor() :
    StateConverter<RoutinesState, RoutinesViewState> {

    override suspend fun convert(state: RoutinesState): RoutinesViewState {
        return when (state) {
            RoutinesState.Idle -> RoutinesViewState.Idle
            RoutinesState.Empty -> RoutinesViewState.Empty
            is RoutinesState.Data -> Data(
                routines = state.routines.map { Routine.from(routine = it) }.asImmutableList(),
                message = state.action?.toMessage()
            )
            is RoutinesState.Error -> RoutinesViewState.Error(throwable = state.throwable)
        }
    }

    private fun Action.toMessage(): Message {
        return when (this) {
            is DeleteRoutineError -> Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )
        }
    }
}
