package com.enricog.features.routines.list.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.core.compose.api.modifiers.draggable.listDraggable
import com.enricog.core.compose.api.modifiers.draggable.rememberListDraggableState
import com.enricog.entities.ID
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.theme.TempoTheme

internal const val RoutinesSceneTestTag = "RoutinesSceneTestTag"

@Composable
internal fun RoutinesScene(
    routinesItems: ImmutableList<RoutinesItem>,
    message: Message?,
    onRoutine: (ID) -> Unit,
    onRoutineDelete: (ID) -> Unit,
    onRoutineDuplicate: (ID) -> Unit,
    onCreateRoutine: () -> Unit,
    onRoutineMoved: (ID, ID) -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    val snackbarHostState = rememberSnackbarHostState()
    if (message != null) {
        val messageText = stringResource(id = message.textResId)
        val actionText = stringResourceOrNull(id = message.actionTextResId)
        LaunchedEffect(snackbarHostState) {
            val event = snackbarHostState.show(message = messageText, actionText = actionText)
            onSnackbarEvent(event)
        }
    }

    val draggableState = rememberListDraggableState(
        key = routinesItems,
        isItemDraggable = { routinesItems[it].isDraggable }
    )
    LaunchedEffect(routinesItems) {
        draggableState.itemMovedEvent.collect { itemMoved ->
            val draggedSegment = routinesItems[itemMoved.indexDraggedItem]
                    as? RoutinesItem.RoutineItem
            val hoveredSegment = routinesItems[itemMoved.indexHoveredItem]
                    as? RoutinesItem.RoutineItem
            if (draggedSegment != null && hoveredSegment != null) {
                onRoutineMoved(draggedSegment.id, hoveredSegment.id)
            }
        }
    }

    TempoSnackbarHost(
        state = snackbarHostState,
        content = {
            Box(
                modifier = Modifier
                    .testTag(RoutinesSceneTestTag)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = draggableState.listState,
                    modifier = Modifier
                        .listDraggable(key = routinesItems, state = draggableState)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(TempoTheme.dimensions.spaceM),
                    contentPadding = PaddingValues(TempoTheme.dimensions.spaceM)
                ) {
                    itemsIndexed(
                        items = routinesItems,
                        key = { _, item ->
                            when(item) {
                                is RoutinesItem.RoutineItem -> item.id.toLong()
                                RoutinesItem.Space -> item.hashCode()
                            }
                        }
                    ) { index, item ->
                        when(item) {
                            is RoutinesItem.RoutineItem -> {
                                val isDragged = draggableState.isDragging && index == draggableState.draggedItem?.index
                                val offsetY = draggableState.hoveredItemIndex?.let { hoveredIndex ->
                                    val draggedItem = draggableState.draggedItem ?: return@let 0f
                                    val itemHeight = draggedItem.size + TempoTheme.dimensions.spaceM.toPx()
                                    when {
                                        draggedItem.index == hoveredIndex -> 0f
                                        index in (draggedItem.index + 1)..hoveredIndex -> itemHeight.times(-1)
                                        index in hoveredIndex until draggedItem.index -> itemHeight
                                        else -> 0f
                                    }
                                } ?: 0f

                                val animate = animateFloatAsState(targetValue = offsetY)
                                if (!isDragged) {
                                    RoutineItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .graphicsLayer {
                                                translationY =
                                                    if (draggableState.isDragging) animate.value else offsetY
                                            },
                                        enableClick = !draggableState.isDragging,
                                        routineItem = item,
                                        onClick = onRoutine,
                                        onDelete = onRoutineDelete,
                                        onDuplicate = onRoutineDuplicate
                                    )
                                } else {
                                    RoutineItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .alpha(0f),
                                        enableClick = false,
                                        routineItem = item,
                                        onClick = { },
                                        onDelete = { },
                                        onDuplicate = onRoutineDuplicate
                                    )
                                }
                            }
                            RoutinesItem.Space -> Spacer(modifier = Modifier.height(75.dp))
                        }
                    }
                }
                if (draggableState.isDragging) {
                    DraggedRoutine(
                        modifier = Modifier.padding(horizontal = TempoTheme.dimensions.spaceM),
                        routine = routinesItems[draggableState.draggedItem!!.index] as RoutinesItem.RoutineItem,
                        offsetProvider = { draggableState.draggedItemOffsetY }
                    )
                }
            }
        },
        anchor = {
            AnimatedVisibility(
                visible = !draggableState.isDragging,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    TempoIconButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(TempoTheme.dimensions.spaceM),
                        onClick = onCreateRoutine,
                        iconResId = R.drawable.ic_add,
                        color = TempoButtonColor.Accent,
                        contentDescription = stringResource(R.string.content_description_button_create_routine)
                    )
                }
            }
        }
    )
}
