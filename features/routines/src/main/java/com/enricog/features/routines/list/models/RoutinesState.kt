package com.enricog.features.routines.list.models

import com.enricog.entities.routines.Routine

internal sealed class RoutinesState {

    object Idle : RoutinesState()

    object Empty : RoutinesState()

    data class Data(val routines: List<Routine>) : RoutinesState()
}
