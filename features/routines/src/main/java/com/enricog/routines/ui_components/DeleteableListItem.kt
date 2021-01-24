package com.enricog.routines.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.enricog.routines.R
import com.enricog.routines.ui_components.SwipeableState.CLOSE
import com.enricog.routines.ui_components.SwipeableState.OPEN
import com.enricog.ui_components.resources.commonShapes
import com.enricog.ui_components.resources.dimensions
import kotlin.math.roundToInt

// FIXME the swipe state is remembered for deleted items; deleted item's state is applied to the one below

private enum class SwipeableState {
    CLOSE,
    OPEN
}

@Composable
internal fun DeletableListItem(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) = WithConstraints {
    val width = constraints.maxWidth.toFloat()
    val openWidth = width * 0.3f

    val swipeState = rememberSwipeableState(CLOSE)
    val anchors = mutableMapOf(0f to CLOSE, -openWidth to OPEN)

    Box(
        modifier = modifier
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.5f)
                    .clip(MaterialTheme.commonShapes.listItem)
                    .background(MaterialTheme.colors.error)
                    .clickable {
                        onDelete()
                        swipeState.snapTo(CLOSE)
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    modifier = Modifier.padding(MaterialTheme.dimensions.spaceM),
                    text = stringResource(R.string.label_delete),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onError,
                )
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) },
            shape = MaterialTheme.commonShapes.listItem,
            content = content
        )
    }
}