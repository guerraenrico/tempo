package com.enricog.timer.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    val count = state.step.count
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag(CountingSceneTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state.step.type.name, style = MaterialTheme.typography.h1)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = state.runningSegment.name, style = MaterialTheme.typography.h1)
        Spacer(modifier = Modifier.height(40.dp))
        Clock(
            backgroundColor = state.clockBackgroundColor,
            timeInSeconds = count.timeInSeconds
        )
        Spacer(modifier = Modifier.height(40.dp))
        ActionsBar(
            isTimeRunning = count.isRunning,
            isRoutineCompleted = false,
            timerActions = timerActions
        )
    }
}