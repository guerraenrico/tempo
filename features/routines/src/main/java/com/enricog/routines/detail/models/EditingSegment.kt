package com.enricog.routines.detail.models

import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

internal sealed class EditingSegment {
    object None : EditingSegment()

    data class Data(
        val segment: Segment,
        val errors: Map<Field.Segment, Int>,
        val timeTypes: List<TimeType>
    ) : EditingSegment()
}