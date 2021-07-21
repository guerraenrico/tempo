package com.enricog.ui_components.common.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.enricog.ui_components.resources.TempoTheme

@Composable
fun TempoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    contentDescription: String,
    icon: Painter? = null,
    color: TempoButtonColor = TempoButtonColor.Normal,
    enabled: Boolean = true
) {
    require(contentDescription.isNotBlank()) { "contentDescription cannot be blank" }
    require(text.isNotBlank()) { "text cannot be blank, use TempoIconButton if you need to show only an icon" }

    CompositionLocalProvider(
        LocalElevationOverlay provides null
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = color
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    tint = color.contentColor(enabled).value,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
            }
            Text(
                text = text,
                style = TempoTheme.typography.button,
                color = color.contentColor(enabled).value
            )
        }
    }
}
