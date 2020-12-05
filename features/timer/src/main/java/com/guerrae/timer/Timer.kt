package com.guerrae.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.viewModel
import com.guerrae.timer.models.TimerViewState
import com.guerrae.timer.ui_components.Clock

@Composable
fun Timer() {
    val viewModel: TimerViewModel = viewModel()
    val viewState = viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val value = viewState.value) {
            TimerViewState.Idle -> {
            }
            is TimerViewState.Counting -> Clock(
                backgroundColor = colors.primary,
                timeInSeconds = value.timeInSeconds
            )
        }
    }
}