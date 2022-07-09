package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import com.enricog.core.compose.api.modifiers.spacing.horizontalListItemSpacing
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.ui.time_type.color
import com.enricog.ui.theme.TempoTheme
import kotlinx.coroutines.launch

@Composable
internal fun SegmentTypeTabs(
    tabSpace: Dp,
    tabWidth: Dp,
    tabAnchors: Map<Float, TimeType>,
    swipeState: SwipeableState<TimeType>,
    timeTypes: List<TimeType>,
    selectedTimeType: TimeType,
    onSelectTimeTypeChange: (TimeType) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .padding(TempoTheme.dimensions.spaceS)
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = selectedTimeType.color(),
                    cornerRadius = CornerRadius(x = 50f, y = 50f),
                    topLeft = Offset(x = swipeState.offset.value, y = tabSpace.toPx()),
                    size = Size(width = tabWidth.toPx(), height = 90f)
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
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            timeTypes.mapIndexed { index, timeType ->
                SegmentTypeTab(
                    value = timeType,
                    isSelected = false,
                    onClick = {
                        coroutineScope.launch {
                            onSelectTimeTypeChange(it)
                            swipeState.animateTo(it)
                        }
                    },
                    modifier = Modifier
                        .horizontalListItemSpacing(
                            itemPosition = index,
                            spacing = tabSpace,
                            includeEdge = false
                        )
                        .width(width = tabWidth)
                )
            }
        }
    }
}
