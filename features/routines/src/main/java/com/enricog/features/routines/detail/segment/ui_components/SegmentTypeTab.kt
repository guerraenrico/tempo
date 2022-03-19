package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.enricog.core.compose.api.modifiers.horizontalListItemSpacing
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.ui_components.TimeTypeChip
import com.enricog.ui.theme.TempoTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SegmentTypeTab(
    modifier: Modifier = Modifier,
    state: SwipeableState<TimeType>,
    timeTypes: List<TimeType>,
    selected: TimeType,
    onSelectChange: (TimeType) -> Unit
) = BoxWithConstraints(modifier = modifier) {
    val width = constraints.maxWidth.toFloat()
    val anchors = timeTypes
        .mapIndexed { index, timeType -> width / (index + 1) to timeType }
        .toMap()
    Row(
        modifier = Modifier
            .padding(TempoTheme.dimensions.spaceS)
            .fillMaxWidth()
            .selectableGroup()
            .swipeable(
                state = state,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
    ) {
        timeTypes.mapIndexed { index, timeType ->
            TimeTypeChip(
                value = timeType,
                isSelected = selected == timeType,
                onClick = onSelectChange,
                modifier = Modifier
                    .horizontalListItemSpacing(
                        itemPosition = index,
                        spacing = TempoTheme.dimensions.spaceS,
                        includeEdge = true
                    )
                    .weight(weight = 1f, fill = true)
            )
        }
    }
}
