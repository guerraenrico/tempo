package com.enricog.routines.detail.summary.models

import com.enricog.entities.routines.Routine

internal sealed class RoutineSummaryState {

    object Idle : RoutineSummaryState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineSummaryField, RoutineSummaryFieldError>
    ) : RoutineSummaryState()
}