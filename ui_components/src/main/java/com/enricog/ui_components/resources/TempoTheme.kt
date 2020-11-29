package com.enricog.ui_components.resources

import androidx.compose.runtime.Composable

@Composable
fun TempoTheme(content: @Composable () -> Unit) {
    content()
}

object TempoTheme {

    @Composable
    val colors: TempoColors
        get() = AmbientColors.current

    @Composable
    val typography: TempoTypography
        get() = AmbientTypography.current

}