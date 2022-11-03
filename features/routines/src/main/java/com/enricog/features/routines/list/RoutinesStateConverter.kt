package com.enricog.features.routines.list

import com.enricog.base.viewmodel.StateConverter
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import javax.inject.Inject

internal class RoutinesStateConverter @Inject constructor() :
    StateConverter<RoutinesState, RoutinesViewState> {

    override suspend fun convert(state: RoutinesState): RoutinesViewState {
        return when (state) {
            RoutinesState.Idle -> RoutinesViewState.Idle
            RoutinesState.Empty -> RoutinesViewState.Empty
            is RoutinesState.Data -> RoutinesViewState.Data(
                routines = state.routines,
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
