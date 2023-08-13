package com.enricog.features.routines.list.models

import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.entities.ID
import com.enricog.features.routines.ui_components.goal_label.GoalLabel
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.TimeText

internal sealed class RoutinesItem {

    abstract val isDraggable: Boolean

    data class RoutineItem(
        @Stable val id: ID,
        val name: String,
        val rank: String,
        val segmentsSummary: SegmentsSummary?,
        val goalLabel: GoalLabel?
    ) : RoutinesItem() {

        override val isDraggable: Boolean = true

        data class SegmentsSummary(
            val estimatedTotalTime: TimeText?,
            val segmentTypesCount: ImmutableMap<TimeTypeStyle, Int>
        )
    }

    object Space : RoutinesItem() {
        override val isDraggable: Boolean = false
    }
}