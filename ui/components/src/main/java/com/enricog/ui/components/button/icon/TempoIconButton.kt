package com.enricog.ui.components.button.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.icon.TempoIcon

@Composable
fun TempoIconButton(
    onClick: () -> Unit,
    icon: Painter,
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
            .background(
                color = color
                    .buttonColors()
                    .backgroundColor(enabled).value,
                shape = CircleShape
            )
            .size(size = size.box),
        enabled = enabled
    ) {
        TempoIcon(
            icon = icon,
            color = color.buttonColors().contentColor(enabled).value,
            size = size.icon,
            contentDescription = contentDescription
        )
    }
}
