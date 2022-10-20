package com.enricog.features.routines.list.models

import androidx.annotation.StringRes
import com.enricog.data.routines.api.entities.Routine

internal sealed class RoutinesViewState {

    object Idle : RoutinesViewState()

    object Empty : RoutinesViewState()

    data class Data(val routines: List<Routine>, val message: Message?) : RoutinesViewState() {
        data class Message(@StringRes val textResId: Int, @StringRes val actionTextResId: Int)
    }

    data class Error(val throwable: Throwable) : RoutinesViewState()
}
