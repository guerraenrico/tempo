package com.enricog.ui_components.common.button

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.enricog.ui_components.resources.FontFamilyDefault

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
                    imageVector = icon.copy(defaultHeight = 14.dp, defaultWidth = 14.dp),
                    tint = color.contentColor(enabled)
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
