package com.enricog.features.routines.list.models

import androidx.annotation.StringRes
import com.enricog.core.compose.api.classes.ImmutableList

internal sealed class RoutinesViewState {

    object Idle : RoutinesViewState()

    object Empty : RoutinesViewState()

    data class Data(
        val routinesItems: ImmutableList<RoutinesItem>,
        val message: Message?
    ) : RoutinesViewState() {
        data class Message(@StringRes val textResId: Int, @StringRes val actionTextResId: Int?)
    }

    data class Error(val throwable: Throwable) : RoutinesViewState()
}
