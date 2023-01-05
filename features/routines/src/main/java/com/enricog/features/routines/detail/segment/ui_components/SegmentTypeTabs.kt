package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.compose.api.modifiers.spacing.horizontalListItemSpacing
import com.enricog.core.compose.api.modifiers.swipeable.FractionalThreshold
import com.enricog.core.compose.api.modifiers.swipeable.SwipeableState
import com.enricog.core.compose.api.modifiers.swipeable.swipeable
import com.enricog.features.routines.detail.ui.time_type.TimeType
import kotlinx.coroutines.launch
import java.lang.Float.min
import kotlin.math.abs

@Composable
internal fun SegmentTypeTabs(
    tabSpace: Dp,
    tabWidth: Dp,
    tabAnchors: ImmutableMap<Float, TimeType>,
    swipeState: SwipeableState<TimeType>,
    timeTypes: ImmutableList<TimeType>,
    selectedTimeType: TimeType,
    onSelectTimeTypeChange: (TimeType) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = selectedTimeType.color,
                    cornerRadius = CornerRadius(x = 50f, y = 50f),
                    topLeft = Offset(x = swipeState.offset.value, y = tabSpace.toPx()),
                    size = Size(width = tabWidth.toPx(), height = 31.dp.toPx())
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .swipeable(
                    state = swipeState,
                    anchors = tabAnchors,
                    enabled = false,
                    reverseDirection = true,
                    thresholds = { _, _ -> FractionalThreshold(fraction = 0.5f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            val anchors = tabAnchors.map { it.value to it.key }.toMap()
            val offset by swipeState.offset
            val offsetRange = anchors[timeTypes[1]] ?: 1f

            timeTypes.forEachIndexed { index, timeType ->
                val anchor = anchors[timeType] ?: 1f

                SegmentTypeTab(
                    progress = min(1f, abs(anchor - offset) / offsetRange),
                    timeType = timeType,
                    onClick = {
                        coroutineScope.launch {
                            onSelectTimeTypeChange(it)
                            swipeState.animateTo(it)
                        }
                    },
                    modifier = Modifier
                        .horizontalListItemSpacing(
                            itemPosition = index,
                            spacingHorizontal = tabSpace,
                            spacingVertical = tabSpace,
                            includeEdge = false
                        )
                        .width(width = tabWidth)
                )
            }
        }
    }
}
