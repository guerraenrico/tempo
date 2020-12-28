package com.enricog.ui_components.resources

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable

@Composable
fun TempoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = defaultThemeColors,
        typography = defaultTypography,
    ) {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

