package com.enricog.features.routines.detail.summary.models

import androidx.annotation.StringRes

internal sealed class RoutineSummaryViewState {

    object Idle : RoutineSummaryViewState()

    data class Data(
        val items: List<RoutineSummaryItem>,
        val message: Message?
    ) : RoutineSummaryViewState() {
        data class Message(@StringRes val textResId: Int, @StringRes val actionTextResId: Int?)
    }

    data class Error(val throwable: Throwable) : RoutineSummaryViewState()
}
