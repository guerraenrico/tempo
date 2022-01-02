package com.enricog.routines.detail.summary.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.routines.detail.summary.models.RoutineSummaryItem.SegmentSectionTitle
import com.enricog.ui_components.modifiers.listDraggable
import com.enricog.ui_components.resources.TempoTheme

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

    val listState = rememberLazyListState()
    val dragState= remember { SegmentDragState() }
    val showHeaderAddSegment by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > summaryItems.indexOfFirst { it is SegmentSectionTitle }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSummarySceneTestTag)
    ) {
        SummaryList(
            modifier = Modifier
                // TODO should this modifier be in the component file? where should be the dragState? should the dragState be a state or just an mutable object?
                .listDraggable(
                    key = summaryItems, // TODO can this be removed? I think is messing with the list scroll state, For now this is needed because the summaryItems instance in the onDragStopped method is the wrong one
                    listState = listState,
                    onDragStarted = { itemIndex: Int, offsetY: Float ->
                        dragState.onDragStarted(itemIndex = itemIndex, offsetY = offsetY)
                    },
                    onDrag = { _, hoveredIndex: Int, offsetY: Float ->
                        dragState.onDrag(hoveredIndex = hoveredIndex, offsetY = offsetY)
                    },
                    onDragStopped = { _, _ ->
                        val item = summaryItems[dragState.indexDraggedItem] as? SegmentItem
                        val hoveredItem = summaryItems[dragState.indexHoveredItem] as? SegmentItem
                        if (item != null) {
                            onSegmentMoved(item.segment, hoveredItem?.segment)
                        }
                        dragState.onDragStopped()
                    },
                    onDragCancelled = { dragState.onDragCancelled() }
                ),
            listState = listState,
            dragState = dragState,
            items = summaryItems,
            onSegmentAdd = onSegmentAdd,
            onSegmentSelected = onSegmentSelected,
            onSegmentDelete = onSegmentDelete,
            onRoutineEdit = onRoutineEdit
        )

        AnimatedVisibility(
            visible = showHeaderAddSegment && !dragState.isDragging,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HeaderAddSegment(
                modifier = Modifier.align(Alignment.TopCenter),
                onAddSegmentClick = onSegmentAdd
            )
        }

        AnimatedVisibility(
            visible = dragState.isDragging,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.padding(horizontal = TempoTheme.dimensions.spaceM)
        ) {
            if (dragState.isDragging) {
                DraggedSegment(
                    item = dragState.getDraggedSegmentItem(summaryItems),
                    offset = dragState.offsetY
                )
            }
        }

        AnimatedVisibility(
            visible = !dragState.isDragging,
            enter = slideInVertically(initialOffsetY = { it / 2 }),
            exit = slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            StartRoutineButton(onClick = onRoutineStart)
        }
    }
}

internal class SegmentDragState {
    var offsetY by mutableStateOf(0f)
        private set
    var indexDraggedItem by mutableStateOf(-1)
        private set
    var indexHoveredItem by mutableStateOf(-1)
        private set
    val isDragging: Boolean
        get() = indexDraggedItem >= 0

    fun onDragStarted(itemIndex: Int, offsetY: Float) {
        this.indexDraggedItem = itemIndex
        this.indexHoveredItem = itemIndex
        this.offsetY = offsetY
    }

    fun onDrag(hoveredIndex: Int, offsetY: Float) {
        this.indexHoveredItem = hoveredIndex
        this.offsetY = offsetY
    }

    fun onDragStopped() {
        clear()
    }

    fun onDragCancelled() {
        clear()
    }

    private fun clear() {
        this.offsetY = 0f
        this.indexDraggedItem = -1
        this.indexHoveredItem = -1
    }

    fun getDraggedSegmentItem(items: List<RoutineSummaryItem>): SegmentItem {
        return items[indexDraggedItem] as SegmentItem
    }
}
