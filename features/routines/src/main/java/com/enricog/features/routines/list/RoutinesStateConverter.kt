package com.enricog.features.routines.list

import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.asImmutableList
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesItem.RoutineItem
import com.enricog.features.routines.list.models.RoutinesItem.Space
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineSuccess
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DuplicateRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import javax.inject.Inject

internal class RoutinesStateConverter @Inject constructor() :
    StateConverter<RoutinesState, RoutinesViewState> {

    override suspend fun convert(state: RoutinesState): RoutinesViewState {
        return when (state) {
            RoutinesState.Idle -> RoutinesViewState.Idle
            RoutinesState.Empty -> RoutinesViewState.Empty
            is RoutinesState.Data -> Data(
                routinesItems = buildList {
                    addAll(state.routines.map { RoutineItem.from(routine = it, timerTheme = state.timerTheme) })
                    add(Space)
                }.asImmutableList(),
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
            DeleteRoutineSuccess -> Message(
                textResId = R.string.label_routines_delete_confirm,
                actionTextResId = R.string.action_text_routines_delete_undo
            )
            MoveRoutineError -> Message(
                textResId = R.string.label_routines_move_error,
                actionTextResId = null
            )
            DuplicateRoutineError -> Message(
                textResId = R.string.label_routines_duplicate_error,
                actionTextResId = null
            )
        }
    }
}
