package com.enricog.features.routines.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.enricog.features.routines.R
import com.enricog.features.routines.ui_components.SwipeableState.CLOSE
import com.enricog.features.routines.ui_components.SwipeableState.OPEN
import com.enricog.ui.components.resources.TempoTheme
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
) = BoxWithConstraints(modifier = modifier) {

    val width = constraints.maxWidth.toFloat()
    val openWidth = width * 0.3f

    val swipeState = rememberSwipeableState(CLOSE)
    val anchors = mutableMapOf(0f to CLOSE, -openWidth to OPEN)

    Box(
        modifier = Modifier
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
                    .clip(TempoTheme.commonShapes.listItem)
                    .background(TempoTheme.colors.error)
                    .clickable { onDelete() },
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    modifier = Modifier.padding(TempoTheme.dimensions.spaceM),
                    text = stringResource(R.string.label_delete),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TempoTheme.colors.onError,
                )
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(x = swipeState.offset.value.roundToInt(), y = 0) },
            shape = TempoTheme.commonShapes.listItem,
            content = content
        )
    }
}
