package com.enricog.routines.detail.summary.models

import com.enricog.entities.routines.Segment

internal sealed class RoutineSummaryItem {

    data class RoutineInfo(
        val routineName: String
    ) : RoutineSummaryItem()

    data class SegmentSectionTitle(
        val error: Pair<RoutineSummaryField.Segments, Int>?
    ) : RoutineSummaryItem()

    data class SegmentItem(
        val segment: Segment
    ) : RoutineSummaryItem()
}