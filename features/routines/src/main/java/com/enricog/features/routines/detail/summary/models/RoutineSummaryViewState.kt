package com.enricog.features.routines.detail.summary.models

internal sealed class RoutineSummaryViewState {

    object Idle : RoutineSummaryViewState()

    data class Data(
        val items: List<RoutineSummaryItem>
    ) : RoutineSummaryViewState()
}
