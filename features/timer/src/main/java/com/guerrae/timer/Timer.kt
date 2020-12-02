package com.guerrae.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.enricog.ui_components.resources.TempoTheme.colors
import com.guerrae.timer.ui_components.Clock

@Composable
fun Timer() {

    Column {
        Clock(backgroundColor = colors.accent, timeInSeconds = 1528)
    }
}