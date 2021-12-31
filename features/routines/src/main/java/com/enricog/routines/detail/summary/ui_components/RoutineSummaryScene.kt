package com.enricog.routines.detail.summary.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.modifiers.listDraggable
import com.enricog.ui_components.resources.TempoTheme
import com.enricog.ui_components.resources.TempoTheme.dimensions

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"
internal const val RoutineSummaryColumnTestTag = "RoutineSummaryColumn"

@Composable
internal fun RoutineSummaryScene(
    summaryItems: List<RoutineSummaryItem>,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onSegmentMoved: (Segment, Int) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit
) {
    val startRoutineButtonSize = TempoIconButtonSize.Large
    val startRoutinePadding = dimensions.spaceM
    val segmentListBottomSpace = startRoutinePadding + startRoutineButtonSize.box
    val listState = rememberLazyListState()

    // TODO can this be moved in a state object?
    var itemDragOffset by remember { mutableStateOf(0f) }
    var indexDraggedItem by remember { mutableStateOf<Int?>(null) }
    val isDragging = indexDraggedItem != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSummarySceneTestTag)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .testTag(RoutineSummaryColumnTestTag)
                .fillMaxSize()
                .listDraggable(
                    key = summaryItems, // TODO can this be removed? I think is messing with the list scroll state, For now this is needed because the summaryItems instance in the onDragStopped method is the wrong one
                    listState = listState,
                    onDragStarted = { itemIndex: Int, offsetY: Float ->
                        itemDragOffset = offsetY
                        indexDraggedItem = itemIndex
                    },
                    onDrag = { _, offsetY: Float ->
                        itemDragOffset = offsetY
                    },
                    onDragStopped = { itemIndex: Int, newIndex: Int ->
                        itemDragOffset = 0f
                        indexDraggedItem = null

                        val item = summaryItems[itemIndex]
                        if (item is RoutineSummaryItem.SegmentItem) {
                            onSegmentMoved(
                                item.segment,
                                newIndex - 2
                            ) // TODO should remove this -2, is the number of non SegmentItem on top of the list
                        }
                    },
                    onDragCancelled = {
                        itemDragOffset = 0f
                        indexDraggedItem = null
                    }
                ),
            verticalArrangement = spacedBy(dimensions.spaceM),
            contentPadding = PaddingValues(dimensions.spaceM)
        ) {
            itemsIndexed(
                items = summaryItems,
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
                        val isDragged = index == indexDraggedItem
                        SegmentItem(
                            segment = item.segment,
                            enableClick = !isDragging,
                            onClick = onSegmentSelected,
                            onDelete = onSegmentDelete,
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0.4f.takeIf { isDragged } ?: 1f)
                        )
                    }
                    RoutineSummaryItem.Space -> Spacer(Modifier.height(segmentListBottomSpace))
                }.exhaustive
            }
        }

        val showHeaderAddSegment by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > summaryItems.indexOfFirst { it is RoutineSummaryItem.SegmentSectionTitle }
            }
        }
        AnimatedVisibility(
            visible = showHeaderAddSegment && !isDragging,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HeaderAddSegment(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                onAddSegmentClick = onSegmentAdd
            )
        }

        AnimatedVisibility(
            visible = isDragging,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.padding(horizontal = dimensions.spaceM)
        ) {
            indexDraggedItem?.let { itemIndex ->
                val item = summaryItems[itemIndex] as? RoutineSummaryItem.SegmentItem ?: return@let
                SegmentItem(
                    segment = item.segment,
                    enableClick = false,
                    onClick = onSegmentSelected,
                    onDelete = onSegmentDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationY = itemDragOffset
                            shadowElevation = 50f
                        }
                        .zIndex(9999f)
                        .clip(TempoTheme.commonShapes.listItem)
                )
            }
        }

        AnimatedVisibility(
            visible = !isDragging,
            enter = slideInVertically(initialOffsetY = { it / 2 }),
            exit = slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            TempoIconButton(
                modifier = Modifier
                    .padding(startRoutinePadding),
                onClick = onRoutineStart,
                icon = painterResource(R.drawable.ic_routine_play),
                color = TempoButtonColor.Accent,
                size = startRoutineButtonSize,
                contentDescription = stringResource(R.string.content_description_button_start_routine)
            )
        }
    }
}
