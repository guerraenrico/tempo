package com.enricog.ui_components.modifiers

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.verticalListItemSpacing(
    itemPosition: Int,
    spacing: Dp,
): Modifier {
    return padding(
        start = spacing,
        end = spacing,
        top = if (itemPosition == 0) spacing else 0.dp,
        bottom = spacing
    )
}

fun Modifier.horizontalListItemSpacing(
    itemPosition: Int,
    spacing: Dp,
): Modifier {
    return padding(
        start = if (itemPosition == 0) spacing else 0.dp,
        end = spacing,
        top = spacing,
        bottom = spacing
    )
}