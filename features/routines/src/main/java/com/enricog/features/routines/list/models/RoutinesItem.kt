package com.enricog.features.routines.list.models

import androidx.compose.runtime.Stable
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID

internal sealed class RoutinesItem {

    abstract val isDraggable: Boolean

    data class RoutineItem(
        @Stable val id: ID,
        val name: String,
        val rank: String
    ) : RoutinesItem() {

        override val isDraggable: Boolean = true

        companion object {
            fun from(routine: Routine) = RoutineItem(
                id = routine.id,
                name = routine.name,
                rank = routine.rank.toString()
            )
        }
    }


    object Space : RoutinesItem() {
        override val isDraggable: Boolean = false
    }
}