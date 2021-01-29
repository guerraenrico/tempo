package com.enricog.ui_components.common.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TempoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    contentDescription: String,
    icon: ImageVector? = null,
    color: TempoButtonColor = TempoButtonColor.Normal,
    enabled: Boolean = true
) {
    require(contentDescription.isNotBlank()) { "contentDescription cannot be blank" }
    require(text.isNotBlank()) { "text cannot be blank" }

    Providers(
        AmbientElevationOverlay provides null
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = color
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    tint = color.contentColor(enabled),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = color.contentColor(enabled)
            )
        }
    }
}
