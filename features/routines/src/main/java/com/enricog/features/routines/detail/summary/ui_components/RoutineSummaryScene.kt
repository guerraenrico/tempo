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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.core.compose.api.modifiers.draggable.listDraggable
import com.enricog.core.compose.api.modifiers.draggable.rememberListDraggableState
import com.enricog.entities.ID
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentSectionTitle
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState.Data.Message
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.theme.TempoTheme

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"

@Composable
internal fun RoutineSummaryScene(
    summaryItems: ImmutableList<RoutineSummaryItem>,
    message: Message?,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (ID) -> Unit,
    onSegmentDelete: (ID) -> Unit,
    onSegmentMoved: (ID, ID?) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {

    val listDraggableState = rememberListDraggableState(key = summaryItems)
    val snackbarHostState = rememberSnackbarHostState()

    if (message != null) {
        val messageText = stringResource(id = message.textResId)
        val actionText = stringResourceOrNull(id = message.actionTextResId)
        LaunchedEffect(snackbarHostState) {
            val event = snackbarHostState.show(message = messageText, actionText = actionText)
            onSnackbarEvent(event)
        }
    }

    LaunchedEffect(summaryItems) {
        listDraggableState.itemMovedEvent.collect { itemMoved ->
            val draggedSegment = summaryItems[itemMoved.indexDraggedItem] as? SegmentItem
            val hoveredSegment = summaryItems[itemMoved.indexHoveredItem] as? SegmentItem
            if (draggedSegment != null) {
                onSegmentMoved(draggedSegment.id, hoveredSegment?.id)
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
                        segment = summaryItems[listDraggableState.draggedItem?.index!!] as SegmentItem,
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
