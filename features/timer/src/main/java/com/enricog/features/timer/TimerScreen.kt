package com.enricog.features.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.ui_components.CountingScene
import com.enricog.features.timer.ui_components.TimerErrorScene

@Composable
internal fun TimerScreen(viewModel: TimerViewModel) {
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        viewState.Compose(
            onToggleTimer = viewModel::onToggleTimer,
            onRestartSegment = viewModel::onRestartSegment,
            onReset = viewModel::onReset,
            onDone = viewModel::onDone,
            onClose = viewModel::onClose,
            onRetryLoad = viewModel::onRetryLoad
        )
    }
}

@Composable
internal fun TimerViewState.Compose(
    onToggleTimer: () -> Unit,
    onRestartSegment: () -> Unit,
    onReset: () -> Unit,
    onDone: () -> Unit,
    onClose: () -> Unit,
    onRetryLoad: () -> Unit
) {
    when (this) {
        TimerViewState.Idle -> Unit
        is TimerViewState.Counting -> CountingScene(
                state = this,
                onToggleTimer = onToggleTimer,
                onRestartSegment = onRestartSegment,
                onReset = onReset,
                onDone = onDone,
                onClose = onClose
            )
        is TimerViewState.Error -> TimerErrorScene(
            throwable = throwable,
            onRetryLoadClick = onRetryLoad
        )
    }
}
