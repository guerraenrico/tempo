package com.enricog.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun TempoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = defaultThemeColors,
        typography = defaultTypography,
    ) {
        Surface(color = TempoTheme.colors.background, content = content)
    }
}

@Suppress("unused")
object TempoTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colors

    val typography: TempoTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTempoTypography.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val dimensions: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current

    val commonShapes: CommonShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalCommonShape.current
}
