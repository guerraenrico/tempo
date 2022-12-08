package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.enricog.features.timer.R
import com.enricog.ui.components.button.icon.TempoIconButton

internal const val ActionBarTestTag = "ActionBarTestTag"
internal const val ButtonStartStopTestTag = "ButtonStartStopTestTag"
internal const val ButtonRestartTestTag = "ButtonRestartTestTag"

@Composable
internal fun ActionsBar(
    isTimeRunning: Boolean,
    onStartStopButtonClick: () -> Unit,
    onRestartSegmentButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier then Modifier
            .fillMaxWidth()
            .testTag(ActionBarTestTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        RestartButton(onClick = onRestartSegmentButtonClick)
        Spacer(modifier = Modifier.width(20.dp))
        StartStopButton(isRunning = isTimeRunning, onClick = onStartStopButtonClick)
    }
}

@Composable
private fun RestartButton(onClick: () -> Unit) {
    TempoIconButton(
        onClick = onClick,
        icon = painterResource(R.drawable.ic_timer_back),
        modifier = Modifier.testTag(ButtonRestartTestTag),
        contentDescription = stringResource(R.string.content_description_button_restart_routine_segment)
    )
}

@Composable
private fun StartStopButton(isRunning: Boolean, onClick: () -> Unit) {
    val icon = if (isRunning) {
        R.drawable.ic_timer_stop
    } else {
        R.drawable.ic_timer_play
    }
    TempoIconButton(
        onClick = onClick,
        icon = painterResource(icon),
        modifier = Modifier.testTag(ButtonStartStopTestTag),
        contentDescription = stringResource(R.string.content_description_button_stop_routine_segment)
    )
}
