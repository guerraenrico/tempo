package com.enricog.ui_components.modifiers

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalListItemSpacing(
    itemPosition: Int,
    spacing: Dp,
    includeEdge: Boolean = true
): Modifier {
    var top = 0.dp
    var bottom = 0.dp

    if (includeEdge) {
        if (itemPosition == 0) {
            top = spacing
        }
        bottom = spacing
    } else {
        if (itemPosition > 0) {
            top = spacing
        }
    }

    return padding(
        start = spacing,
        end = spacing,
        top = top,
        bottom = bottom
    )
}

fun Modifier.horizontalListItemSpacing(
    itemPosition: Int,
    spacing: Dp,
    includeEdge: Boolean = true
): Modifier {
    var start = 0.dp
    var end = 0.dp

    if (includeEdge) {
        if (itemPosition == 0) {
            start = spacing
        }
        end = spacing
    } else {
        if (itemPosition > 0) {
            start = spacing
        }
    }

    return padding(
        start = start,
        end = end,
        top = spacing,
        bottom = spacing
    )
}