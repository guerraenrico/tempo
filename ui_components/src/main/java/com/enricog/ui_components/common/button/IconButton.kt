package com.enricog.ui_components.common.button

import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enricog.ui_components.resources.blue500
import com.enricog.ui_components.resources.darkBlue500
import com.enricog.ui_components.resources.green500
import com.enricog.ui_components.resources.white


@Composable
fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: IconButtonColor = IconButtonColor.Normal,
    size: IconButtonSize = IconButtonSize.Normal,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .shadow(4.dp, shape = CircleShape)
            .background(color.color, CircleShape)
            .size(size = size.box)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                interactionState = remember { InteractionState() },
                indication = rememberRippleIndication(
                    bounded = false,
                    radius = size.box
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon.copy(defaultHeight = size.icon, defaultWidth = size.icon), tint = white)
    }
}


enum class IconButtonSize(val box: Dp, val icon: Dp) {
    Normal(55.dp, 14.dp),
    Large(75.dp, 20.dp)
}

enum class IconButtonColor(val color: Color) {
    Normal(darkBlue500),
    Confirm(green500),
    Accent(blue500)
}