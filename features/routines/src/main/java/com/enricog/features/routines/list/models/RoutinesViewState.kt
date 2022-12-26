package com.enricog.features.routines.list.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.entities.ID
import com.enricog.data.routines.api.entities.Routine as RoutineEntity

internal sealed class RoutinesViewState {

    object Idle : RoutinesViewState()

    object Empty : RoutinesViewState()

    data class Data(
        val routines: ImmutableList<Routine>,
        val message: Message?
    ) : RoutinesViewState() {

        data class Routine(
            @Stable val id: ID,
            val name: String,
            val rank: String
        ) {

            companion object {
                fun from(routine: RoutineEntity) = Routine(
                    id = routine.id,
                    name = routine.name,
                    rank = routine.rank.toString()
                )
            }
        }

        data class Message(@StringRes val textResId: Int, @StringRes val actionTextResId: Int?)
    }

    data class Error(val throwable: Throwable) : RoutinesViewState()
}
