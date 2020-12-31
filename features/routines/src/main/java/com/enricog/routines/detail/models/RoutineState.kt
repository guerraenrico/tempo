package com.enricog.routines.detail.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

internal sealed class RoutineState {
    object Idle : RoutineState()

    data class Data(
        val routine: Routine,
        val errors: Map<Field.Routine, ValidationError>,
        val editingSegment: EditingSegment
    ) : RoutineState()
}

internal sealed class EditingSegment {
    object None : EditingSegment()

    data class Data(
        val originalSegmentPosition: Int,
        val segment: Segment,
        val errors: Map<Field.Segment, ValidationError>,
        val timeTypes: List<TimeType>
    ) : EditingSegment()
}