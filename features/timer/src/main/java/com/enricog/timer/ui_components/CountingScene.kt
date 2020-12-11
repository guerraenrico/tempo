package com.enricog.timer.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState

internal const val CountingSceneTestTag = "CountingSceneTestTag"

@Composable
internal fun CountingScene(state: TimerViewState.Counting, timerActions: TimerActions) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag(CountingSceneTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Clock(
            backgroundColor = MaterialTheme.colors.primary,
            timeInSeconds = state.timeInSeconds
        )
        Spacer(modifier = Modifier.height(40.dp))
        ActionsBar(state.isRunning, timerActions)
    }
}