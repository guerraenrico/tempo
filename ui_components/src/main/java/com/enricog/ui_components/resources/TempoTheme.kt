package com.enricog.ui_components.resources

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.lifecycle.ViewModelProvider
import com.enricog.ui_components.extensions.AmbientViewModelFactory

@Composable
fun TempoTheme(factory: ViewModelProvider.Factory, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = defaultThemeColors,
        typography = defaultTypography,
    ) {
        Providers(
            AmbientViewModelFactory provides factory
        ) {
            Surface(color = MaterialTheme.colors.background) {
                content()
            }
        }
    }
}

