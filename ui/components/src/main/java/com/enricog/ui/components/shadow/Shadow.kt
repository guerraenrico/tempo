package com.enricog.ui.components.shadow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.unit.Dp
import com.enricog.ui.theme.black
import com.enricog.ui.theme.white

@Composable
fun Shadow(
    size: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Spacer(
            Modifier
                .offset(y = (-size))
                .matchParentSize()
                .drawBehind {
                    val center = this.size.height / 2
                    drawCircle(
                        radialGradient(
                            colors = listOf(white.copy(alpha = 0.3f), white.copy(alpha = 0f)),
                            center = Offset(center, center),
                            radius = center
                        )
                    )
                }
        )
        Spacer(
            Modifier
                .offset(y = size)
                .matchParentSize()
                .drawBehind {
                    val center = this.size.height / 2
                    drawCircle(
                        radialGradient(
                            colors = listOf(black.copy(alpha = 1f), black.copy(alpha = 0f)),
                            center = Offset(center, center),
                            radius = center
                        )
                    )
                }
        )
        content()
    }
}
