package com.guerrae.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import com.guerrae.timer.ui_components.Clock

@Composable
fun Timer() {

    Column {
        Clock(backgroundColor = colors.primary, timeInSeconds = 1528)
    }
}