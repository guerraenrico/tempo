package com.enricog.routines.detail.summary.models

import com.enricog.entities.routines.Routine

internal sealed class RoutineSummaryViewState {

    object Idle : RoutineSummaryViewState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineSummaryField, Int>
    ) : RoutineSummaryViewState()
}