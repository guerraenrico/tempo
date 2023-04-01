package com.enricog.features.routines.list.models

import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.compose.api.classes.asImmutableMap
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import com.enricog.core.entities.seconds
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText

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
            val estimatedTotalTime: TimeText?,
            val segmentTypesCount: ImmutableMap<TimeType, Int>
        )

        companion object {
            fun from(routine: Routine): RoutineItem {
                val segmentsSummary = if (routine.segments.isNotEmpty()) {
                    SegmentsSummary(
                        estimatedTotalTime = routine.expectedTotalTime.takeIf { it > 0.seconds }?.timeText,
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