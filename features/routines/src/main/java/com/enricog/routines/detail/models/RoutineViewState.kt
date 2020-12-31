package com.enricog.routines.detail.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

internal sealed class RoutineViewState {
    object Idle : RoutineViewState()

    data class Data(
        val routine: Routine,
        val errors: Map<Field.Routine, Int>,
        val editingSegment: EditingSegmentView
    ) : RoutineViewState()

}

internal sealed class EditingSegmentView {
    object None : EditingSegmentView()

    data class Data(
        val segment: Segment,
        val errors: Map<Field.Segment, Int>,
        val timeTypes: List<TimeType>
    ) : EditingSegmentView()
}