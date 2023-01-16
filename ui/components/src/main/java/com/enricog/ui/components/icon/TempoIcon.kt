package com.enricog.ui.components.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.modifiers.semantics.drawableId

@Composable
fun TempoIcon(
    @DrawableRes iconResId: Int,
    contentDescription: String,
    size: TempoIconSize,
    color: Color,
    modifier: Modifier = Modifier
) {
    require(contentDescription.isNotBlank()) { "contentDescription cannot be blank" }

    Icon(
        painter = painterResource(id = iconResId),
        tint = color,
        modifier = modifier
            .semantics { drawableId(resId = iconResId) }
            .size(size.value),
        contentDescription = contentDescription
    )
}
