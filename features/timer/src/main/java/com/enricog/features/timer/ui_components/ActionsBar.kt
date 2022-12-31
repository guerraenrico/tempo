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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.enricog.features.timer.models.TimerViewState.Counting.Actions
import com.enricog.ui.components.button.icon.TempoIconButton

internal const val ActionBarTestTag = "ActionBarTestTag"
internal const val ButtonToggleStartTestTag = "ButtonToggleStartTestTag"
internal const val ButtonRestartTestTag = "ButtonRestartTestTag"

@Composable
internal fun ActionsBar(
    timerActions: Actions,
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
        RestartButton(
            restartButton = timerActions.restart,
            onClick = onRestartSegmentButtonClick
        )
        Spacer(modifier = Modifier.width(20.dp))
        ToggleStartButton(
            toggleStartButton = timerActions.toggleStart,
            onClick = onStartStopButtonClick
        )
    }
}

@Composable
private fun RestartButton(restartButton: Actions.Button, onClick: () -> Unit) {
    TempoIconButton(
        onClick = onClick,
        iconResId = restartButton.iconResId,
        modifier = Modifier.testTag(ButtonRestartTestTag),
        contentDescription = stringResource(restartButton.contentDescriptionResId),
        size = restartButton.size
    )
}

@Composable
private fun ToggleStartButton(toggleStartButton: Actions.Button, onClick: () -> Unit) {
    TempoIconButton(
        onClick = onClick,
        iconResId = toggleStartButton.iconResId,
        modifier = Modifier.testTag(ButtonToggleStartTestTag),
        contentDescription = stringResource(toggleStartButton.contentDescriptionResId),
        size = toggleStartButton.size
    )
}
