package com.enricog.ui_components.surfaces

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

private val shape = RoundedCornerShape(10.dp)

@Composable
fun ListItemSurface(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = shape,
        content = content
    )
}
