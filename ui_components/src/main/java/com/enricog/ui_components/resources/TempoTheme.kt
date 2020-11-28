package com.enricog.ui_components.resources

import androidx.compose.runtime.Composable

@Composable
fun TempoTheme(content: @Composable () -> Unit) {
    val colors: ThemeColors = defaultThemeColors
    val typography: Typography = defaultTypography


    content()
}