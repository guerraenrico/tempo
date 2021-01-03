package com.enricog.ui_components.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TempoIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: TempoButtonColor = TempoButtonColor.Normal,
    size: TempoIconButtonSize = TempoIconButtonSize.Normal,
    drawShadow: Boolean = true,
    enabled: Boolean = true
) {
    val shadowSize = if (drawShadow) size.shadow else 0.dp
    IconButton(
        onClick = onClick,
        modifier = modifier
            .shadow(shadowSize, shape = CircleShape)
            .background(color = color.backgroundColor(enabled), shape = CircleShape)
            .size(size = size.box),
        enabled = enabled
    ) {
        Icon(icon.copy(defaultHeight = size.icon, defaultWidth = size.icon), tint = color.contentColor(enabled))
    }
}

enum class TempoIconButtonSize(val box: Dp, val icon: Dp, val shadow: Dp) {
    Small(48.dp, 10.dp, 2.dp),
    Normal(55.dp, 14.dp, 4.dp),
    Large(65.dp, 18.dp, 8.dp)
}
