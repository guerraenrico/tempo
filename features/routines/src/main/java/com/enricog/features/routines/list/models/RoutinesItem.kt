package com.enricog.features.routines.list.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.entities.ID
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.TimeText

internal sealed class RoutinesItem {

    abstract val isDraggable: Boolean

    data class RoutineItem(
        @Stable val id: ID,
        val name: String,
        val rank: String,
        val segmentsSummary: SegmentsSummary?,
        val goalText: GoalText?
    ) : RoutinesItem() {

        override val isDraggable: Boolean = true

        data class SegmentsSummary(
            val estimatedTotalTime: TimeText?,
            val segmentTypesCount: ImmutableMap<TimeTypeStyle, Int>
        )

        data class GoalText(
            @StringRes val stringResId: Int,
            val formatArgs: ImmutableList<Any>
        )
    }

    object Space : RoutinesItem() {
        override val isDraggable: Boolean = false
    }
}