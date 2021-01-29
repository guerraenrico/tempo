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
    contentDescription: String,
    modifier: Modifier = Modifier,
    color: TempoButtonColor = TempoButtonColor.Normal,
    size: TempoIconButtonSize = TempoIconButtonSize.Normal,
    drawShadow: Boolean = true,
    enabled: Boolean = true
) {
    require(contentDescription.isNotBlank()) { "contentDescription cannot be blank" }

    val shadowSize = if (drawShadow) size.shadow else 0.dp
    IconButton(
        onClick = onClick,
        modifier = modifier
            .shadow(shadowSize, shape = CircleShape)
            .background(color = color.backgroundColor(enabled), shape = CircleShape)
            .size(size = size.box),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            tint = color.contentColor(enabled),
            modifier = Modifier.size(size.icon),
            contentDescription = contentDescription
        )
    }
}

enum class TempoIconButtonSize(val box: Dp, val icon: Dp, val shadow: Dp) {
    Small(48.dp, 12.dp, 2.dp),
    Normal(55.dp, 14.dp, 4.dp),
    Large(65.dp, 18.dp, 8.dp)
}
