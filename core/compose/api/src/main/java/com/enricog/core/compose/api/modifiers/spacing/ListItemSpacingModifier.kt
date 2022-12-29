package com.enricog.core.compose.api.modifiers.spacing

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalListItemSpacing(
    itemPosition: Int,
    spacingVertical: Dp,
    spacingHorizontal: Dp,
    includeEdge: Boolean = true
): Modifier {
    var top = 0.dp
    var bottom = 0.dp

    if (includeEdge) {
        if (itemPosition == 0) {
            top = spacingVertical
        }
        bottom = spacingVertical
    } else {
        if (itemPosition > 0) {
            top = spacingVertical
        }
    }

    return padding(
        start = spacingHorizontal,
        end = spacingHorizontal,
        top = top,
        bottom = bottom
    )
}

fun Modifier.horizontalListItemSpacing(
    itemPosition: Int,
    spacingVertical: Dp,
    spacingHorizontal: Dp,
    includeEdge: Boolean = true
): Modifier {
    var start = 0.dp
    var end = 0.dp

    if (includeEdge) {
        if (itemPosition == 0) {
            start = spacingHorizontal
        }
        end = spacingHorizontal
    } else {
        if (itemPosition > 0) {
            start = spacingHorizontal
        }
    }

    return padding(
        start = start,
        end = end,
        top = spacingVertical,
        bottom = spacingVertical
    )
}
