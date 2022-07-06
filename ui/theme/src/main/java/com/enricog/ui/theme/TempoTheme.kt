package com.enricog.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun TempoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = TempoTheme.colors.toMaterialColors(),
        typography = TempoTheme.typography.toMaterialTypography(),
        shapes = TempoTheme.shapes.toMaterialShapes()
    ) {
        Surface(color = TempoTheme.colors.background, content = content)
    }
}

object TempoTheme {
    val colors: TempoColors
        @Composable
        @ReadOnlyComposable
        get() = LocalTempoColors.current

    val typography: TempoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTempoTypography.current

    val shapes: TempoShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalTempoShapes.current

    val dimensions: TempoDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalTempoDimensions.current
}
