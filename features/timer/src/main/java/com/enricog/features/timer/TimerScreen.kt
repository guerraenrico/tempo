package com.enricog.features.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.base.extensions.exhaustive
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.ui_components.CountingScene

@Composable
internal fun TimerScreen(viewModel: TimerViewModel) {
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        viewState.Compose(
            onStartStopButtonClick = viewModel::onStartStopButtonClick,
            onRestartSegmentButtonClick = viewModel::onRestartSegmentButtonClick,
            onResetButtonClick = viewModel::onResetButtonClick,
            onDoneButtonClick = viewModel::onDoneButtonClick,
            onCloseButtonClick = viewModel::onCloseButtonClick,
        )
    }
}

@Composable
internal fun TimerViewState.Compose(
    onStartStopButtonClick: () -> Unit,
    onRestartSegmentButtonClick: () -> Unit,
    onResetButtonClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit
) {
    when (this) {
        TimerViewState.Idle -> {
        }
        is TimerViewState.Counting -> {
            CountingScene(
                state = this,
                onStartStopButtonClick = onStartStopButtonClick,
                onRestartSegmentButtonClick = onRestartSegmentButtonClick,
                onResetButtonClick = onResetButtonClick,
                onDoneButtonClick = onDoneButtonClick,
                onCloseButtonClick = onCloseButtonClick
            )
        }
    }.exhaustive
}
