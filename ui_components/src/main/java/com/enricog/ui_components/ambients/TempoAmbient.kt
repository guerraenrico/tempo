package com.enricog.ui_components.ambients

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers

@Composable
fun ProvideTempoAmbient(
    content: @Composable () -> Unit
) {
    Providers(content = content)
}