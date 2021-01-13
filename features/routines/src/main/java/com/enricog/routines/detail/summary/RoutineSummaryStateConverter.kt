package com.enricog.routines.detail.summary

import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import javax.inject.Inject

internal class RoutineSummaryStateConverter @Inject constructor() :
    StateConverter<RoutineSummaryState, RoutineSummaryViewState> {

    override suspend fun convert(state: RoutineSummaryState): RoutineSummaryViewState {
        return when (state) {
            RoutineSummaryState.Idle -> RoutineSummaryViewState.Idle
            is RoutineSummaryState.Data -> RoutineSummaryViewState.Data(routine = state.routine)
        }
    }

}