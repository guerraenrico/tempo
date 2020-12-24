package com.enricog.routines.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RoutinesList() {
    Column(
        modifier = Modifier.background(colors.background)
    ) {
        Text("routines list", style = typography.h1)
    }
}