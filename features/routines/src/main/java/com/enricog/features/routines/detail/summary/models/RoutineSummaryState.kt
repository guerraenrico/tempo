package com.enricog.features.routines.detail.summary.models

import com.enricog.data.routines.api.entities.Routine

internal sealed class RoutineSummaryState {

    object Idle : RoutineSummaryState()

    data class Data(
        val routine: Routine,
        val errors: Map<RoutineSummaryField, RoutineSummaryFieldError>
    ) : RoutineSummaryState()
}
