package com.enricog.ui_components.common.button

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TempoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    color: TempoButtonColor = TempoButtonColor.Normal,
    enabled: Boolean = true
) {
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
                    icon.copy(defaultHeight = 14.dp, defaultWidth = 14.dp),
                    tint = color.contentColor(enabled)
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.button,
                fontWeight = FontWeight.Bold,
                color = color.contentColor(enabled)
            )
        }
    }
}
