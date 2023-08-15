package com.enricog.features.routines.detail.summary.test

import androidx.compose.ui.graphics.Color
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.entities.ID
import com.enricog.core.entities.asID
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.ui_components.goal_label.GoalLabel
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText

internal fun RoutineSummaryViewStateData(block: RoutineSummaryViewStateBuilder.Data.() -> Unit): RoutineSummaryViewState.Data {
    return RoutineSummaryViewStateBuilder.Data().apply(block).build()
}

internal class RoutineSummaryViewStateBuilder {

    class Data {

        var items: ImmutableList<RoutineSummaryItem> = immutableListOf()
        var message: RoutineSummaryViewState.Data.Message? = null

        fun build(): RoutineSummaryViewState.Data {
            return RoutineSummaryViewState.Data(items = items, message = message)
        }
    }
}

internal fun RoutineSummaryItemSegmentItem(block: RoutineSummaryItemBuilder.SegmentItem.() -> Unit): RoutineSummaryItem.SegmentItem {
    return RoutineSummaryItemBuilder.SegmentItem().apply(block).build()
}

internal fun RoutineSummaryItemRoutineInfo(block: RoutineSummaryItemBuilder.RoutineInfo.() -> Unit): RoutineSummaryItem.RoutineInfo {
    return RoutineSummaryItemBuilder.RoutineInfo().apply(block).build()
}

internal fun RoutineSummaryItemSegmentSectionTitle(block: RoutineSummaryItemBuilder.SegmentSectionTitle.() -> Unit): RoutineSummaryItem.SegmentSectionTitle {
    return RoutineSummaryItemBuilder.SegmentSectionTitle().apply(block).build()
}

internal class RoutineSummaryItemBuilder {

    class RoutineInfo {

        var routineName: String = "Routine Name"
        var goalLabel: GoalLabel? = null
        var segmentsSummary: RoutineSummaryItem.RoutineInfo.SegmentsSummary? = null

        fun build(): RoutineSummaryItem.RoutineInfo {
            return RoutineSummaryItem.RoutineInfo(
                routineName = routineName,
                goalLabel = goalLabel,
                segmentsSummary = segmentsSummary
            )
        }
    }

    class SegmentItem {

        var id: ID = 1.asID
        var name: String = "Segment"
        var time: TimeText = "10".timeText
        var rank: String = "aaaaaa"
        var type: TimeTypeStyle = TimeTypeStyle.TIMER

        fun build(): RoutineSummaryItem.SegmentItem {
            return RoutineSummaryItem.SegmentItem(
                id = id,
                name = name,
                time = time,
                type = type,
                rank = rank
            )
        }
    }

    class SegmentSectionTitle {

        var error: Pair<RoutineSummaryField.Segments, Int>? = null

        fun build(): RoutineSummaryItem.SegmentSectionTitle {
            return RoutineSummaryItem.SegmentSectionTitle(error = error)
        }
    }
}

internal val TimeTypeStyle.Companion.TIMER: TimeTypeStyle
    get() = TimeTypeStyle(
        nameStringResId = R.string.chip_time_type_timer_name,
        backgroundColor = Color.Red,
        onBackgroundColor = Color.White,
        id = "TIMER"
    )

internal val TimeTypeStyle.Companion.STOPWATCH: TimeTypeStyle
    get() = TimeTypeStyle(
        nameStringResId = R.string.chip_time_type_stopwatch_name,
        backgroundColor = Color.Black,
        onBackgroundColor = Color.White,
        id = "STOPWATCH"
    )



