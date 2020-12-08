package com.enricog.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.ActionsBar
import com.enricog.timer.ui_components.Clock
import androidx.compose.runtime.getValue

internal const val CountingSceneTestTag = "CountingSceneTestTag"

@Composable
fun Timer() {
    val viewModel: TimerViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        viewModel.ComposeViewState()
    }
}

@Composable
internal fun TimerViewModel.ComposeViewState() {
    val viewState by viewState.collectAsState(TimerViewState.Idle)
    when (val state = viewState) {
        TimerViewState.Idle -> {
        }
        is TimerViewState.Counting -> CountingScene(state, this)
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