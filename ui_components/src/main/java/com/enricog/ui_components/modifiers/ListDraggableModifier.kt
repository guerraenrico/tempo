package com.enricog.ui_components.modifiers

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Modifier.listDraggable(
    key: Any,
    listState: LazyListState,
    onDragStarted: (itemIndex: Int, offsetY: Float) -> Unit = { _, _ -> },
    onDragStopped: (itemIndex: Int, hoveredIndex: Int) -> Unit = { _, _ -> },
    onDragCancelled: () -> Unit = {},
    onDrag: (itemIndex: Int, hoveredIndex: Int, offsetY: Float) -> Unit
): Modifier = composed {

    // TODO find another way to represent the state, this is confusing and hard to track
    var draggedItemOffset by remember { mutableStateOf(0f) }
    var draggedItem by remember { mutableStateOf<LazyListItemInfo?>(null) }
    var draggedItemCurrentIndex by remember { mutableStateOf<Int?>(null) }

    pointerInput(key) {
        coroutineScope {
            detectDragGesturesAfterLongPress(
                onDrag = { change, dragAmount -> // TODO do I need to consumeAllChanges? probably not
                    if (dragAmount.isUnspecified) {
                        draggedItemOffset = 0f
                        draggedItem = null
                        draggedItemCurrentIndex = null

                        onDragCancelled()
                        return@detectDragGesturesAfterLongPress
                    }

                    draggedItemOffset += dragAmount.y

                    withNonNull(draggedItem, draggedItemCurrentIndex) { item, currentIndex ->
                        val startOffset = draggedItemOffset
                        val endOffset = item.size + draggedItemOffset
                        listState.visibleItems
                            .getOrNull(currentIndex - listState.visibleItems.first().index)
                            ?.let { currentHoveredItem ->
                                listState.visibleItems
                                    .filterNot { itemInfo ->
                                        itemInfo.offsetEnd < startOffset || itemInfo.offset > endOffset || currentHoveredItem.index == itemInfo.index
                                    }
                                    .firstOrNull { itemInfo ->
                                        val itemOffsetDelta = startOffset - currentHoveredItem.offset
                                        when {
                                            itemOffsetDelta > 0 -> endOffset > itemInfo.offsetEnd
                                            else -> startOffset > itemInfo.offset && startOffset < itemInfo.offsetEnd
                                        }
                                    }
                                    ?.let { itemInfo ->
                                        draggedItemCurrentIndex = itemInfo.index
                                    }
                            }

                        onDrag(item.index, draggedItemCurrentIndex!!, draggedItemOffset)

                        launch {
                            listState.overScrollOffset(item, draggedItemOffset)
                                .takeIf { it != 0f }
                                ?.let { offset ->
                                    listState.scrollBy(offset)
                                }
                        }
                    }
                },
                onDragStart = { dragAmount ->
                    draggedItem = listState.draggedItemInfo(dragAmount.y)?.also { item ->
                        val index = item.index

                        draggedItemCurrentIndex = index
                        draggedItemOffset += item.offset
                        onDragStarted(index, draggedItemOffset)
                    }
                },
                onDragEnd = {
                    if (draggedItem?.index == draggedItemCurrentIndex) {
                        onDragCancelled()
                    } else {
                        withNonNull(draggedItem, draggedItemCurrentIndex) { item, currentIndex ->
                            onDragStopped(item.index, currentIndex)
                        }

                        draggedItemOffset = 0f
                        draggedItemCurrentIndex = null
                        draggedItem = null
                    }
                },
                onDragCancel = {
                    onDragCancelled()

                    draggedItemOffset = 0f
                    draggedItem = null
                    draggedItemCurrentIndex = null
                }
            )
        }
    }
}

fun LazyListState.overScrollOffset(
    draggedItem: LazyListItemInfo,
    draggedItemOffset: Float
): Float {
    val startOffset = draggedItemOffset
    val endOffset = draggedItem.size + draggedItemOffset

    return when {
        draggedItemOffset > 0 ->
            (endOffset - layoutInfo.viewportEndOffset).takeIf { diff -> diff > 0 } ?: 0f
        draggedItemOffset < 0 ->
            (startOffset - layoutInfo.viewportStartOffset).takeIf { diff -> diff < 0 } ?: 0f
        else -> 0f
    }
}

private fun LazyListState.draggedItemInfo(offsetY: Float): LazyListItemInfo? {
    return layoutInfo.visibleItemsInfo
        .firstOrNull { item -> offsetY.toInt() in item.offset..item.offsetEnd }
}

private val LazyListState.visibleItems: List<LazyListItemInfo>
    get() = layoutInfo.visibleItemsInfo

private val LazyListItemInfo.offsetEnd: Int
    get() = offset + size

private fun <T : Any, V : Any> withNonNull(value1: T?, value2: V?, block: (T, V) -> Unit) {
    if (value1 != null && value2 != null) {
        block(value1, value2)
    }
}
