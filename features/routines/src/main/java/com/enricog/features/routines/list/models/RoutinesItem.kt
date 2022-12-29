package com.enricog.features.routines.list.models

import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.compose.api.classes.asImmutableMap
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.ui.time_type.TimeType

internal sealed class RoutinesItem {

    abstract val isDraggable: Boolean

    data class RoutineItem(
        @Stable val id: ID,
        val name: String,
        val rank: String,
        val segmentsSummary: SegmentsSummary?
    ) : RoutinesItem() {

        override val isDraggable: Boolean = true

        data class SegmentsSummary(
            @Stable val totalTime: Seconds?,
            val segmentTypesCount: ImmutableMap<TimeType, Int>
        )

        companion object {
            fun from(routine: Routine): RoutineItem {
                val segmentsSummary = if (routine.segments.isNotEmpty()) {
                    SegmentsSummary(
                        totalTime = routine.segments.map { it.time }
                            .reduce { acc, time -> acc + time }
                            .takeIf { it > 0.seconds },
                        segmentTypesCount = routine.segments.groupBy { it.type }
                            .map { (type, segments) -> TimeType.from(type) to segments.size }
                            .toMap()
                            .asImmutableMap()
                    )
                } else null

                return RoutineItem(
                    id = routine.id,
                    name = routine.name,
                    rank = routine.rank.toString(),
                    segmentsSummary = segmentsSummary
                )
            }
        }
    }

    object Space : RoutinesItem() {
        override val isDraggable: Boolean = false
    }
}