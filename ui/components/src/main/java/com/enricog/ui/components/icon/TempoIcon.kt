package com.enricog.ui.components.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
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

    val internalModifier = Modifier
        .semantics { drawableId(resId = iconResId) }
        .apply {
            if (size != TempoIconSize.Original) {
                size(size.value)
            }
        }

    Icon(
        painter = painterResource(id = iconResId),
        tint = color,
        modifier = modifier.then(internalModifier),
        contentDescription = contentDescription
    )
}



