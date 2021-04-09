package com.enricog.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.CountingScene

@Composable
internal fun TimerScreen(routineId: Long, viewModel: TimerViewModel) {
    DisposableEffect(routineId) {
        viewModel.load(TimerConfiguration(routineId))
        onDispose {}
    }
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
        is TimerViewState.Counting ->{
            CountingScene(state = this, timerActions = timerActions)
        }
    }.exhaustive
}
