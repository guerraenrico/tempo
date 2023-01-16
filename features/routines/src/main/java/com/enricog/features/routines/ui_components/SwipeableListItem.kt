package com.enricog.features.routines.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.modifiers.swipeable.FractionalThreshold
import com.enricog.core.compose.api.modifiers.swipeable.rememberSwipeableState
import com.enricog.core.compose.api.modifiers.swipeable.swipeable
import com.enricog.features.routines.R
import com.enricog.features.routines.ui_components.SwipeableState.CLOSE
import com.enricog.features.routines.ui_components.SwipeableState.DELETE
import com.enricog.features.routines.ui_components.SwipeableState.DUPLICATE
import com.enricog.ui.components.icon.TempoIcon
import com.enricog.ui.components.icon.TempoIconSize
import com.enricog.ui.theme.TempoTheme
import kotlin.math.roundToInt

private enum class SwipeableState {
    CLOSE,
    DELETE,
    DUPLICATE
}

@Composable
internal fun SwipeableListItem(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    content: @Composable () -> Unit,
) = BoxWithConstraints(modifier = modifier) {

    val width = constraints.maxWidth.toFloat()

    val swipeState = rememberSwipeableState(
        CLOSE,
        confirmStateChange = {
            when (it) {
                CLOSE -> {
                    true
                }
                DELETE -> {
                    onDelete()
                    false
                }
                DUPLICATE -> {
                    onDuplicate()
                    false
                }
            }

        }
    )
    val anchors = mutableMapOf(0f to CLOSE, -width to DELETE, width to DUPLICATE)
    val (backgroundColor, textColor) = when {
        swipeState.offset.value > 0f -> TempoTheme.colors.secondary to TempoTheme.colors.onSecondary
        swipeState.offset.value < 0f -> TempoTheme.colors.error to TempoTheme.colors.onError
        else -> Color.Transparent to Color.Transparent
    }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.6f) },
                orientation = Orientation.Horizontal,
                velocityThreshold = 100000.dp
            )
    ) {
        Box(
            modifier = Modifier.matchParentSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(1f)
                    .clip(TempoTheme.shapes.listItem)
                    .background(backgroundColor)
                    .padding(horizontal = TempoTheme.dimensions.spaceM)
            ) {
                if (swipeState.offset.value < 0f) {
                    TempoIcon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        iconResId = R.drawable.ic_delete,
                        contentDescription = stringResource(id = R.string.label_delete),
                        size = TempoIconSize.ExtraLarge,
                        color = textColor
                    )
                }
                if (swipeState.offset.value > 0f) {
                    TempoIcon(
                        modifier = Modifier.align(Alignment.CenterStart),
                        iconResId = R.drawable.ic_duplicate,
                        contentDescription = stringResource(id = R.string.label_duplicate),
                        size = TempoIconSize.ExtraLarge,
                        color = textColor
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(x = swipeState.offset.value.roundToInt(), y = 0) }
                .clip(TempoTheme.shapes.listItem)
                .background(TempoTheme.colors.surface),
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}
