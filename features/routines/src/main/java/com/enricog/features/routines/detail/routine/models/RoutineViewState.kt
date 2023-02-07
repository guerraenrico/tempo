package com.enricog.features.routines.detail.routine.models

import androidx.annotation.StringRes
import com.enricog.core.compose.api.classes.ImmutableMap

internal sealed class RoutineViewState {

    object Idle : RoutineViewState()

    data class Data(
        val errors: ImmutableMap<RoutineField, RoutineFieldError>,
        val message: Message?
    ) : RoutineViewState() {
        data class Message(@StringRes val textResId: Int, @StringRes val actionTextResId: Int)
    }

    data class Error(val throwable: Throwable) : RoutineViewState()
}
