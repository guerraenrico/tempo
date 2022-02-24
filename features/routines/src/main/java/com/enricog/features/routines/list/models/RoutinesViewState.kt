package com.enricog.features.routines.list.models

import com.enricog.entities.routines.Routine

internal sealed class RoutinesViewState {

    object Idle : RoutinesViewState()

    object Empty : RoutinesViewState()

    data class Data(val routines: List<Routine>) : RoutinesViewState()
}
