package com.enricog.routines.detail.summary.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.routines.detail.summary.models.RoutineSummaryItem.SegmentSectionTitle
import com.enricog.ui_components.modifiers.listDraggable
import com.enricog.ui_components.modifiers.rememberListDraggableState
import com.enricog.ui_components.resources.TempoTheme
import kotlinx.coroutines.flow.collect

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"

@Composable
internal fun RoutineSummaryScene(
    summaryItems: List<RoutineSummaryItem>,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onSegmentMoved: (Segment, Segment?) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit
) {

    val listDraggableState = rememberListDraggableState(key = summaryItems)

    LaunchedEffect(summaryItems) {
        listDraggableState.itemMovedEvent.collect { itemMoved ->
            val item = summaryItems[itemMoved.indexDraggedItem] as? SegmentItem
            val hoveredItem = summaryItems[itemMoved.indexHoveredItem] as? SegmentItem
            if (item != null) {
                onSegmentMoved(item.segment, hoveredItem?.segment)
            }
        }
    }

    val showHeaderAddSegment by remember {
        derivedStateOf {
            listDraggableState.firstVisibleItemIndex > summaryItems.indexOfFirst { it is SegmentSectionTitle }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSummarySceneTestTag)
    ) {
        SummaryList(
            modifier = Modifier.listDraggable(key = summaryItems, state = listDraggableState),
            dragState = listDraggableState,
            items = summaryItems,
            onSegmentAdd = onSegmentAdd,
            onSegmentSelected = onSegmentSelected,
            onSegmentDelete = onSegmentDelete,
            onRoutineEdit = onRoutineEdit
        )

        AnimatedVisibility(
            visible = showHeaderAddSegment && !listDraggableState.isDragging,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HeaderAddSegment(
                modifier = Modifier.align(Alignment.TopCenter),
                onAddSegmentClick = onSegmentAdd
            )
        }

        if (listDraggableState.isDragging) {
            DraggedSegment(
                modifier = Modifier.padding(horizontal = TempoTheme.dimensions.spaceM),
                item = summaryItems[listDraggableState.draggedItem?.index!!] as SegmentItem,
                offset = listDraggableState.draggedItemOffsetY
            )
        }

        AnimatedVisibility(
            visible = !listDraggableState.isDragging,
            enter = slideInVertically(initialOffsetY = { it / 2 }),
            exit = slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            StartRoutineButton(onClick = onRoutineStart)
        }
    }
}
