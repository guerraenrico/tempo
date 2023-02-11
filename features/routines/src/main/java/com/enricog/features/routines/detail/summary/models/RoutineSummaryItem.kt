package com.enricog.features.routines.detail.summary.models

import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.data.routines.api.entities.Segment
import com.enricog.entities.ID
import com.enricog.entities.Seconds
import com.enricog.features.routines.detail.ui.time_type.TimeType

internal sealed class RoutineSummaryItem {

    abstract val isDraggable: Boolean

    data class RoutineInfo(
        val routineName: String,
        val segmentsSummary: SegmentsSummary?
    ) : RoutineSummaryItem() {
        override val isDraggable: Boolean = false

        data class SegmentsSummary(
            @Stable val totalTime: Seconds?,
            val segmentTypesCount: ImmutableMap<TimeType, Int>
        )
    }

    data class SegmentSectionTitle(
        @Stable val error: Pair<RoutineSummaryField.Segments, Int>?
    ) : RoutineSummaryItem() {
        override val isDraggable: Boolean = false
    }

    data class SegmentItem(
        @Stable val id: ID,
        val name: String,
        @Stable val time: Seconds,
        val type: TimeType,
        val rank: String
    ) : RoutineSummaryItem() {
        override val isDraggable: Boolean = true

        companion object {
            fun from(segment: Segment) = SegmentItem(
                id = segment.id,
                name = segment.name,
                time = segment.time,
                type = TimeType.from(segment.type),
                rank = segment.rank.toString()
            )
        }
    }

    object Space : RoutineSummaryItem() {
        override val isDraggable: Boolean = false
    }
}
