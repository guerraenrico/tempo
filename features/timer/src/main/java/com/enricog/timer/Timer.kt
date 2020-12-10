package com.enricog.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.ActionsBar
import com.enricog.timer.ui_components.Clock

internal const val CountingSceneTestTag = "CountingSceneTestTag"

/**
 * TODO: fix test
 */

@Composable
internal fun Timer(viewModel: TimerViewModel) {
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
    }
}

@Composable
private fun CountingScene(state: TimerViewState.Counting, timerActions: TimerActions) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag(CountingSceneTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Clock(
            backgroundColor = colors.primary,
            timeInSeconds = state.timeInSeconds
        )
        Spacer(modifier = Modifier.height(40.dp))
        ActionsBar(state.isRunning, timerActions)
    }
}