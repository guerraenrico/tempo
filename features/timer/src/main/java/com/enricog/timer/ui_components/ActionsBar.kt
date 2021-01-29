package com.enricog.timer.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.enricog.timer.R
import com.enricog.timer.models.TimerActions
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton

internal const val ActionBarTestTag = "ActionBarTestTag"
internal const val ButtonDoneTestTag = "ButtonDoneTestTag"
internal const val ButtonResetTestTag = "ButtonResetTestTag"
internal const val ButtonStartStopTestTag = "ButtonStartStopTestTag"
internal const val ButtonRestartTestTag = "ButtonRestartTestTag"

@Composable
internal fun ActionsBar(
    isTimeRunning: Boolean,
    isRoutineCompleted: Boolean,
    timerActions: TimerActions,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier then Modifier
            .fillMaxWidth()
            .testTag(ActionBarTestTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isRoutineCompleted) {
            DoneActions(
                onResetButtonClick = timerActions::onResetButtonClick,
                onDoneButtonClick = timerActions::onDoneButtonClick
            )
        } else {
            RunningActions(
                isTimeRunning = isTimeRunning,
                onRestartSegmentButtonClick = timerActions::onRestartSegmentButtonClick,
                onStartStopButtonClick = timerActions::onStartStopButtonClick
            )
        }

    }
}

@Composable
private fun RunningActions(
    isTimeRunning: Boolean,
    onRestartSegmentButtonClick: () -> Unit,
    onStartStopButtonClick: () -> Unit
) {
    RestartButton(onClick = onRestartSegmentButtonClick)
    Spacer(modifier = Modifier.width(20.dp))
    StartStopButton(isRunning = isTimeRunning, onClick = onStartStopButtonClick)
}

@Composable
private fun DoneActions(
    onResetButtonClick: () -> Unit,
    onDoneButtonClick: () -> Unit
) {
    TempoButton(
        onClick = onResetButtonClick,
        text = stringResource(R.string.button_reset),
        color = TempoButtonColor.Normal,
        modifier = Modifier.testTag(ButtonResetTestTag),
        contentDescription = stringResource(R.string.content_description_button_reset_routine)
    )
    Spacer(modifier = Modifier.width(20.dp))
    TempoButton(
        onClick = onDoneButtonClick,
        text = stringResource(R.string.button_done),
        color = TempoButtonColor.Accent,
        modifier = Modifier.testTag(ButtonDoneTestTag),
        contentDescription = stringResource(R.string.content_description_button_done_routine)
    )
}

@Composable
private fun RestartButton(onClick: () -> Unit) {
    TempoIconButton(
        onClick = onClick,
        icon = vectorResource(R.drawable.ic_timer_restart),
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
        icon = vectorResource(icon),
        modifier = Modifier.testTag(ButtonStartStopTestTag),
        contentDescription = stringResource(R.string.content_description_button_stop_routine_segment)
    )
}