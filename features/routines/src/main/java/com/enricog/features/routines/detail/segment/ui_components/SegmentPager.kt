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
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun SegmentPager(
    timeTypeStyles: ImmutableList<TimeTypeStyle>,
    selectedType: TimeTypeStyle,
    onSelectTimeTypeChange: (TimeTypeStyle) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = BoxWithConstraints(modifier = modifier) {

    val swipeState = rememberSwipeableState(
        initialValue = selectedType,
        valueSaver = TimeTypeStyle.Saver()
    ) {
        onSelectTimeTypeChange(it)
        true
    }
    val draggableState = rememberDraggableState {
        swipeState.drag(it)
    }

    val tabSpace = TempoTheme.dimensions.spaceS
    val tabWidth = (maxWidth - (tabSpace * (timeTypeStyles.size - 1))) / timeTypeStyles.size
    val tabAnchors = timeTypeStyles
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
            timeTypeStyles = timeTypeStyles,
            selectedTimeTypeStyle = selectedType,
            onSelectTimeTypeChange = onSelectTimeTypeChange,
        )
        content()
    }
}