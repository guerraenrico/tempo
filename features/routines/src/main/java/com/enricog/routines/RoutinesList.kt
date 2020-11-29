package com.enricog.routines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.enricog.ui_components.resources.TempoTheme.colors
import com.enricog.ui_components.resources.TempoTheme.typography

@Composable
fun RoutinesList() {
    Column(
        modifier = Modifier.background(colors.background)
    ) {
        BasicText("routines list", style = typography.title)
    }
}