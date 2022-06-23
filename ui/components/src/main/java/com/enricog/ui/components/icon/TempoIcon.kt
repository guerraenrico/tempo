package com.enricog.ui.components.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun TempoIcon(
    icon: Painter,
    contentDescription: String,
    size: TempoIconSize,
    color: Color,
    modifier: Modifier = Modifier
) {
    require(contentDescription.isNotBlank()) { "contentDescription cannot be blank" }

    Icon(
        painter = icon,
        tint = color,
        modifier = modifier.size(size.value),
        contentDescription = contentDescription
    )
}



