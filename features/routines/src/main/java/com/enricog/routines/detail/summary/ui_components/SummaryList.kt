package com.enricog.routines.detail.summary.ui_components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.extensions.toPx
import com.enricog.ui_components.modifiers.ListDraggableState
import com.enricog.ui_components.resources.TempoTheme

internal const val RoutineSummaryColumnTestTag = "RoutineSummaryColumn"

@Composable
internal fun SummaryList(
    modifier: Modifier = Modifier,
    dragState: ListDraggableState,
    items: List<RoutineSummaryItem>,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onRoutineEdit: () -> Unit
) {
    LazyColumn(
        state = dragState.listState,
        modifier = modifier
            .testTag(RoutineSummaryColumnTestTag)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(TempoTheme.dimensions.spaceM),
        contentPadding = PaddingValues(TempoTheme.dimensions.spaceM)
    ) {
        itemsIndexed(
            items = items,
            key = { _, item ->
                when (item) {
                    is RoutineSummaryItem.RoutineInfo -> item.hashCode()
                    is RoutineSummaryItem.SegmentSectionTitle -> item.hashCode()
                    is RoutineSummaryItem.SegmentItem -> item.segment.id.toLong()
                    RoutineSummaryItem.Space -> item.hashCode()
                }
            }
        ) { index, item ->
            when (item) {
                is RoutineSummaryItem.RoutineInfo ->
                    RoutineSection(
                        routineName = item.routineName,
                        onEditRoutine = onRoutineEdit
                    )
                is RoutineSummaryItem.SegmentSectionTitle ->
                    SegmentSectionTitle(item = item, onAddSegmentClick = onSegmentAdd)
                is RoutineSummaryItem.SegmentItem -> {
                    val isDragged = dragState.isDragging && index == dragState.draggedItem?.index
                    val offsetY = dragState.hoveredItemIndex?.let { hoveredIndex ->
                        val draggedItem = dragState.draggedItem ?: return@let 0f
                        val itemHeight = draggedItem.size + TempoTheme.dimensions.spaceM.toPx()
                        when {
                            draggedItem.index == hoveredIndex -> 0f
                            index in (draggedItem.index + 1)..hoveredIndex -> itemHeight.times(-1)
                            index in (hoveredIndex + 1) until draggedItem.index -> itemHeight
                            else -> 0f
                        }
                    } ?: 0f

                    val animate = animateFloatAsState(targetValue = offsetY)
                    if (!isDragged) {
                        SegmentItem(
                            segment = item.segment,
                            enableClick = !dragState.isDragging,
                            onClick = onSegmentSelected,
                            onDelete = onSegmentDelete,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    translationY = if (dragState.isDragging) animate.value else offsetY
                                }
                        )
                    } else {
                        SegmentItem(
                            segment = item.segment,
                            enableClick = false,
                            onClick = { },
                            onDelete = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0f)
                        )
                    }
                }
                RoutineSummaryItem.Space -> Spacer(Modifier.height(TempoTheme.dimensions.spaceM + TempoIconButtonSize.Large.box))
            }.exhaustive
        }
    }
}