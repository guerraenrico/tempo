package com.enricog.features.routines.detail.summary.models

import androidx.compose.runtime.Stable
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.data.routines.api.entities.Segment
import com.enricog.core.entities.ID
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText

internal sealed class RoutineSummaryItem {

    abstract val isDraggable: Boolean

    data class RoutineInfo(
        val routineName: String,
        val segmentsSummary: SegmentsSummary?
    ) : RoutineSummaryItem() {
        override val isDraggable: Boolean = false

        data class SegmentsSummary(
            val estimatedTotalTime: TimeText?,
            val segmentTypesCount: ImmutableMap<TimeTypeStyle, Int>
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
        val time: TimeText,
        val type: TimeTypeStyle,
        val rank: String
    ) : RoutineSummaryItem() {
        override val isDraggable: Boolean = true

        companion object {
            fun from(segment: Segment, timerTheme: TimerTheme) = SegmentItem(
                id = segment.id,
                name = segment.name,
                time = segment.time.timeText,
                type = TimeTypeStyle.from(timeType = segment.type, timerTheme = timerTheme),
                rank = segment.rank.toString()
            )
        }
    }

    object Space : RoutineSummaryItem() {
        override val isDraggable: Boolean = false
    }
}
