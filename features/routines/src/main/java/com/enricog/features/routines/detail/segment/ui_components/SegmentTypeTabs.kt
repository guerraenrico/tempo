package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ExperimentalMaterialApi
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
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.core.compose.api.modifiers.horizontalListItemSpacing
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.ui.time_type.color
import com.enricog.ui.theme.TempoTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SegmentTypeTabs(
    modifier: Modifier = Modifier,
    state: SwipeableState<TimeType>,
    timeTypes: List<TimeType>,
    selected: TimeType,
    onSelectChange: (TimeType) -> Unit
) = BoxWithConstraints(modifier = modifier) {
    val width = maxWidth
    val tabSpace = TempoTheme.dimensions.spaceS
    val tabWidth = (width - (tabSpace * (timeTypes.size + 1))) / timeTypes.size
    val anchors = timeTypes
        .mapIndexed { index, timeType -> (tabWidth.toPx() * index) + (tabSpace.toPx() * index) to timeType }
        .toMap()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .padding(TempoTheme.dimensions.spaceS)
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = selected.color(),
                    cornerRadius = CornerRadius(x = 50f, y = 50f),
                    topLeft = Offset(x = state.offset.value, y = tabSpace.toPx()),
                    size = Size(width = tabWidth.toPx(), height = 90f)
                )
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .swipeable(
                    state = state,
                    anchors = anchors,
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
                            onSelectChange(it)
                            state.animateTo(it)
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
