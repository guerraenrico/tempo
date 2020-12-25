package com.enricog.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.CountingScene

@Composable
internal fun Timer(viewModel: TimerViewModel) {
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        viewState.Compose(viewModel)
    }
}

@Composable
internal fun TimerViewState.Compose(timerActions: TimerActions) {
    when (this) {
        TimerViewState.Idle -> {
        }
        is TimerViewState.Counting -> CountingScene(state = this, timerActions = timerActions)
    }.exhaustive
}