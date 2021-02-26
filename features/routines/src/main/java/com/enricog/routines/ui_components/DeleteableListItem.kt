package com.enricog.routines.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

private enum class SwipeableState {
    CLOSE,
    OPEN
}

@Composable
internal fun DeletableListItem(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) = BoxWithConstraints {
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
                    .clickable { onDelete() },
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
                .offset { IntOffset(x = swipeState.offset.value.roundToInt(), y = 0) },
            shape = MaterialTheme.commonShapes.listItem,
            content = content
        )
    }
}
