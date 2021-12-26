package com.enricog.ui_components.modifiers

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.listDraggable(
    listState: LazyListState,
    onDragStarted: (itemIndex: Int) -> Unit = {},
    onDrag: (itemIndex: Int, offsetY: Float) -> Unit = { _, _ -> },
    onDragStopped: (itemIndex: Int, newIndex: Int) -> Unit = { _, _ -> },
    onDragCancelled: () -> Unit = {}
): Modifier = composed {

    var draggedItemOffset by remember { mutableStateOf(0f) }
    var draggedItem by remember { mutableStateOf<LazyListItemInfo?>(null) }
    var draggedItemCurrentIndex by remember { mutableStateOf<Int?>(null) }

    pointerInput(listState) {
        detectDragGesturesAfterLongPress(
            onDrag = { _, dragAmount ->
                draggedItemOffset += dragAmount.y

                draggedItem?.let { item ->
                    val startOffset = item.offset + draggedItemOffset
                    val endOffset = item.offsetEnd + draggedItemOffset

                    draggedItemCurrentIndex?.let { currentIndex ->
                        listState.layoutInfo.visibleItemsInfo
                            .getOrNull(currentIndex - listState.layoutInfo.visibleItemsInfo.first().index)
                            ?.let { hoveredItem ->
                                listState.layoutInfo.visibleItemsInfo
                                    .filterNot { item ->
                                        item.offsetEnd < startOffset || item.offset > endOffset || hoveredItem.index == item.index
                                    }
                                    .firstOrNull { item ->
                                        val itemOffsetDelta = startOffset - hoveredItem.offset
                                        when {
                                            itemOffsetDelta > 0 -> (endOffset > (item.offset + item.size))
                                            else -> startOffset < item.offset
                                        }
                                    }
                                    ?.let { item ->
                                        draggedItemCurrentIndex = item.index
                                    }
                            }
                    }

                    onDrag(item.index, draggedItemOffset)
                }
            },
            onDragStart = { dragAmount ->
                draggedItem = listState.draggedItemInfo(dragAmount.y)?.also { item ->
                    draggedItemCurrentIndex = item.index
                    onDragStarted(item.index)
                }
            },
            onDragEnd = {
                withNonNull(draggedItem, draggedItemCurrentIndex) { item, currentIndex ->
                    onDragStopped(item.index, currentIndex)
                }

                draggedItemCurrentIndex = null
                draggedItem = null
            },
            onDragCancel = {
                onDragCancelled()

                draggedItem = null
                draggedItemCurrentIndex = null
            }
        )
    }
}

private fun LazyListState.draggedItemInfo(offsetY: Float): LazyListItemInfo? {
    return layoutInfo.visibleItemsInfo
        .firstOrNull { item -> offsetY.toInt() in item.offset..item.offsetEnd }
}

private val LazyListItemInfo.offsetEnd: Int
    get() = offset + size

private fun <T : Any, V : Any> withNonNull(value1: T?, value2: V?, block: (T, V) -> Unit) {
    if (value1 != null && value2 != null) {
        block(value1, value2)
    }
}
