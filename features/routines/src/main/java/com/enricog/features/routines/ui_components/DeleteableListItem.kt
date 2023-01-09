package com.enricog.features.routines.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.enricog.core.compose.api.modifiers.swipeable.FractionalThreshold
import com.enricog.core.compose.api.modifiers.swipeable.rememberSwipeableState
import com.enricog.core.compose.api.modifiers.swipeable.swipeable
import com.enricog.features.routines.R
import com.enricog.features.routines.ui_components.SwipeableState.*
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import kotlin.math.roundToInt

private enum class SwipeableState {
    CLOSE,
    DELETE,
    DUPLICATE
}

@Composable
internal fun DeletableListItem(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    content: @Composable () -> Unit,
) = BoxWithConstraints(modifier = modifier) {

    val width = constraints.maxWidth.toFloat()
    val openWidth = width * 0.5f

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
    val anchors = mutableMapOf(0f to CLOSE, -openWidth to DELETE, openWidth to DUPLICATE)
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
                thresholds = { _, _ -> FractionalThreshold(0.8f) },
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
                    .fillMaxWidth(1f)
                    .clip(TempoTheme.shapes.listItem)
                    .background(backgroundColor)
            ) {
                TempoText(
                    modifier = Modifier
                        .padding(TempoTheme.dimensions.spaceM)
                        .align(Alignment.CenterEnd),
                    text = stringResource(R.string.label_delete),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 12.sp
                    )
                )
                TempoText(
                    modifier = Modifier
                        .padding(TempoTheme.dimensions.spaceM)
                        .align(Alignment.CenterStart),
                    text = stringResource(R.string.label_duplicate),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 12.sp
                    )
                )
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
