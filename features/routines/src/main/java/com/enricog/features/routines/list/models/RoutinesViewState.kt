package com.enricog.features.routines.list.models

import com.enricog.data.routines.api.entities.Routine

internal sealed class RoutinesViewState {

    object Idle : RoutinesViewState()

    object Empty : RoutinesViewState()

    data class Data(val routines: List<Routine>) : RoutinesViewState()

    data class Error(val throwable: Throwable) : RoutinesViewState()
}
