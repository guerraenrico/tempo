package com.enricog.timer.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.enricog.timer.R
import com.enricog.timer.models.TimerActions
import com.enricog.ui_components.common.button.IconButton

internal const val ActionBarTestTag = "ActionBarTestTag"

@Composable
internal fun ActionsBar(isTimeRunning: Boolean, timerActions: TimerActions) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(ActionBarTestTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        RestartButton(onClick = timerActions::onRestartButtonClick)
        Spacer(modifier = Modifier.width(20.dp))
        StartStopButton(isRunning = isTimeRunning, onClick = timerActions::onStartStopButtonClick)
    }
}

@Composable
private fun RestartButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, icon = vectorResource(R.drawable.ic_timer_restart))
}

@Composable
private fun StartStopButton(isRunning: Boolean, onClick: () -> Unit) {
    val icon = if (isRunning) {
        R.drawable.ic_timer_stop
    } else {
        R.drawable.ic_timer_play
    }
    IconButton(onClick = onClick, icon = vectorResource(icon))
}