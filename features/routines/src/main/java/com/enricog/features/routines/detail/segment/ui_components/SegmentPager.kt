package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.toImmutableMap
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.core.compose.api.modifiers.swipeable.rememberSwipeableState
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun SegmentPager(
    timeTypes: ImmutableList<TimeType>,
    selectedType: TimeType,
    onSelectTimeTypeChange: (TimeType) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = BoxWithConstraints(modifier = modifier) {

    val swipeState = rememberSwipeableState(
        initialValue = selectedType,
        valueSaver = TimeType.Saver()
    ) {
        onSelectTimeTypeChange(it)
        true
    }
    val draggableState = rememberDraggableState {
        swipeState.drag(it)
    }

    val tabSpace = TempoTheme.dimensions.spaceS
    val tabWidth = (maxWidth - (tabSpace * (timeTypes.size - 1))) / timeTypes.size
    val tabAnchors = timeTypes
        .mapIndexed { index, timeType -> (tabWidth.toPx() * index) + (tabSpace.toPx() * index) to timeType }
        .toImmutableMap()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(initial = 0))
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                reverseDirection = true,
                onDragStopped = { swipeState.fling(it) }
            )
    ) {
        SegmentTypeTabs(
            tabSpace = tabSpace,
            tabWidth = tabWidth,
            tabAnchors = tabAnchors,
            swipeState = swipeState,
            timeTypes = timeTypes,
            selectedTimeType = selectedType,
            onSelectTimeTypeChange = onSelectTimeTypeChange,
        )
        content()
    }
}