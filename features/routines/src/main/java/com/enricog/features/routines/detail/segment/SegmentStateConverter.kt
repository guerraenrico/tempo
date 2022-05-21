package com.enricog.features.routines.detail.segment

import com.enricog.base.viewmodel.StateConverter
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import javax.inject.Inject

internal class SegmentStateConverter @Inject constructor() :
    StateConverter<SegmentState, SegmentViewState> {

    override suspend fun convert(state: SegmentState): SegmentViewState {
        return when (state) {
            SegmentState.Idle -> SegmentViewState.Idle
            is SegmentState.Data -> state.toViewState()
        }
    }

    private fun SegmentState.Data.toViewState(): SegmentViewState {
        return SegmentViewState.Data(
            segment = SegmentFields(
                name = inputs.name,
                time = inputs.time,
                type = inputs.type
            ),
            errors = errors,
            timeTypes = timeTypes
        )
    }
}
