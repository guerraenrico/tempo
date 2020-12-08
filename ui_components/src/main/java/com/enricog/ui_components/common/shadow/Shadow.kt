package com.enricog.ui_components.common.shadow

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enricog.ui_components.resources.white

// TODO review trying to remove contentSize
@Composable
fun Shadow(
    contentSize: Dp,
    size: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Spacer(
            Modifier
                .offset(y = (-size))
                .size(size = contentSize + 2.dp)
                .drawBehind {
                    val center = this.size.height / 2
                    drawCircle(
                        RadialGradient(
                            colors = listOf(white.copy(alpha = 0.3f), white.copy(alpha = 0f)),
                            centerX = center,
                            centerY = center,
                            radius = center
                        )
                    )
                }
        )
        Spacer(
            Modifier
                .offset(y = size)
                .size(size = contentSize + 2.dp)
                .drawBehind {
                    val center = this.size.height / 2
                    drawCircle(
                        RadialGradient(
                            colors = listOf(Color.Black.copy(alpha = 1f), white.copy(alpha = 0f)),
                            centerX = center,
                            centerY = center,
                            radius = center
                        )
                    )
                }
        )
        content()
    }
}