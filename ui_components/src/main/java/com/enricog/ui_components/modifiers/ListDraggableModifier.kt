package com.enricog.ui_components.modifiers

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.debugInspectorInfo
import com.enricog.core.coroutine.job.autoCancelableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Create a [ListDraggableState] that will be remembered until the [key] will change.
 * The [key] should be a value that changes when the list is updated, in this way a new [ListDraggableState]
 * will be created and will work with the updated [listState].
 * Usually the [key] parameter is the list of item displayed.
 */
@Composable
fun rememberListDraggableState(
    key: Any,
    scope: CoroutineScope = rememberCoroutineScope(),
    listState: LazyListState = rememberLazyListState()
): ListDraggableState {
    return remember(key) { ListDraggableState(listState = listState, scope = scope) }
}

fun Modifier.listDraggable(
    key: Any,
    state: ListDraggableState
): Modifier = composed(
    debugInspectorInfo {
        name = "listDraggable"
        properties["key"] = key
        properties["state"] = state
    }
) {
    pointerInput(key) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, dragAmount ->
                state.onDrag(dragAmount)
                change.consumeAllChanges()
            },
            onDragStart = { dragAmount ->
                state.onDragStarted(dragAmount)
            },
            onDragEnd = {
                state.onDragEnded()
            },
            onDragCancel = {
                state.onDragCancelled()
            }
        )
    }
}

class ListDraggableState(
    private val listState: LazyListState,
    private val scope: CoroutineScope
) {

    private var scrollJob by autoCancelableJob()

    var draggedItemOffsetY by mutableStateOf(0f)
        private set
    var draggedItem by mutableStateOf<LazyListItemInfo?>(null)
        private set
    var hoveredItemIndex by mutableStateOf<Int?>(null)
        private set

    val isDragging: Boolean
        get() = draggedItem != null

    private val itemMovedChannel = Channel<ItemMoved>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val itemMovedEvent: Flow<ItemMoved> = itemMovedChannel.receiveAsFlow()

    internal fun onDragStarted(dragAmount: Offset) {
        draggedItem = listState.draggedItemInfo(dragAmount.y)?.also { item ->
            val index = item.index
            hoveredItemIndex = index
            draggedItemOffsetY += item.offset
        }
    }

    internal fun onDrag(dragAmount: Offset) {
        if (dragAmount.isUnspecified) {
            clear()
            return
        }

        draggedItemOffsetY += dragAmount.y

        withNonNull(draggedItem, hoveredItemIndex) { item, currentIndex ->
            val startOffset = draggedItemOffsetY
            val endOffset = item.size + draggedItemOffsetY
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
                        ?.let { itemInfo -> hoveredItemIndex = itemInfo.index }
                }

            listState.overScrollOffset(item, draggedItemOffsetY)
                .takeIf { it != 0f }
                ?.let(::scrollListBy)
        }
    }

    internal fun onDragEnded() {
        if (draggedItem?.index == hoveredItemIndex) {
            clear()
        } else {
            withNonNull(draggedItem, hoveredItemIndex) { item, hoveredIndex ->
                itemMovedChannel.trySend(ItemMoved(item.index, hoveredIndex))
            }
        }
    }

    internal fun onDragCancelled() {
        clear()
    }

    private fun clear() {
        this.draggedItemOffsetY = 0f
        this.draggedItem = null
        this.hoveredItemIndex = null
    }

    private fun scrollListBy(offset: Float) {
        scrollJob = scope.launch {
            listState.scrollBy(offset)
        }
    }

    private fun LazyListState.overScrollOffset(
        draggedItem: LazyListItemInfo,
        draggedItemOffset: Float
    ): Float {
        return when {
            draggedItemOffset > 0 ->
                (draggedItem.size + draggedItemOffset - layoutInfo.viewportEndOffset)
                    .takeIf { diff -> diff > 0 } ?: 0f
            draggedItemOffset < 0 ->
                (draggedItemOffset - layoutInfo.viewportStartOffset)
                    .takeIf { diff -> diff < 0 } ?: 0f
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
}

data class ItemMoved(val indexDraggedItem: Int, val indexHoveredItem: Int)
