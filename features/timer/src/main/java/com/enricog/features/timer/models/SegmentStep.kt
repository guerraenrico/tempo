package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Segment

internal data class SegmentStep(
    val id: Int,
    val segment: Segment,
    val count: Count,
    val type: SegmentStepType,
    val routineRound: Int,
    val segmentRound: Int
)
