package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui.theme.TempoTheme

internal const val DraggedSegmentTestTag = "DraggedSegment"

@Composable
internal fun DraggedSegment(
    modifier: Modifier = Modifier,
    item: RoutineSummaryItem.SegmentItem,
    offset: Float
) {
    SegmentItem(
        segment = item.segment,
        enableClick = false,
        onClick = {},
        onDelete = {},
        modifier = modifier
            .testTag(DraggedSegmentTestTag)
            .fillMaxWidth()
            .graphicsLayer {
                translationY = offset
                shadowElevation = 50f
            }
            .zIndex(9999f)
            .clip(TempoTheme.shapes.listItem)
    )
}