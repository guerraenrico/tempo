package com.enricog.routines.detail.summary.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.enricog.ui_components.resources.TempoTheme.dimensions

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"
internal const val RoutineSummaryColumnTestTag = "RoutineSummaryColumn"

@Composable
internal fun RoutineSummaryScene(
    summaryItems: List<RoutineSummaryItem>,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit
) {
    val startRoutineButtonSize = TempoIconButtonSize.Large
    val startRoutinePadding = dimensions.spaceM
    val segmentListBottomSpace = startRoutinePadding + startRoutineButtonSize.box
    val listState = rememberLazyListState()

    var itemDragOffset by remember { mutableStateOf(0f) }
    var indexDraggedItem by remember { mutableStateOf<Int?>(null) }

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
                    listState = listState,
                    onDrag = { itemIndex: Int, offsetY: Float ->
                        itemDragOffset = offsetY
                        indexDraggedItem = itemIndex
                    },
                    onDragStopped = { itemIndex: Int, newIndex: Int ->
                        itemDragOffset = 0f
                        indexDraggedItem = null
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
                            onClick = onSegmentSelected,
                            onDelete = onSegmentDelete,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {

                                    translationY = itemDragOffset.takeIf { isDragged } ?: 0f
                                    scaleX = 1.05f.takeIf { isDragged } ?: 1f
                                    scaleY = 1.05f.takeIf { isDragged } ?: 1f
                                }
                                .zIndex(9999f.takeIf { isDragged } ?: 0f)
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
            visible = showHeaderAddSegment,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            HeaderAddSegment(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                onAddSegmentClick = onSegmentAdd
            )
        }

        TempoIconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(startRoutinePadding),
            onClick = onRoutineStart,
            icon = painterResource(R.drawable.ic_routine_play),
            color = TempoButtonColor.Accent,
            size = startRoutineButtonSize,
            contentDescription = stringResource(R.string.content_description_button_start_routine)
        )
    }
}
