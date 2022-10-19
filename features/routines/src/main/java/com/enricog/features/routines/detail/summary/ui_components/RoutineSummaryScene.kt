package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.core.compose.api.modifiers.draggable.listDraggable
import com.enricog.core.compose.api.modifiers.draggable.rememberListDraggableState
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentSectionTitle
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.theme.TempoTheme

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
    val snackbarHostState = rememberSnackbarHostState()
    val scope = rememberCoroutineScope()

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

    TempoSnackbarHost(
        state = snackbarHostState,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(RoutineSummarySceneTestTag)
            ) {
                SummaryList(
                    modifier = Modifier.listDraggable(
                        key = summaryItems,
                        state = listDraggableState
                    ),
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
            }
        },
        anchor = {
            AnimatedVisibility(
                visible = !listDraggableState.isDragging,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    StartRoutineButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onClick = onRoutineStart
                    )
                }
            }
        }
    )
}
