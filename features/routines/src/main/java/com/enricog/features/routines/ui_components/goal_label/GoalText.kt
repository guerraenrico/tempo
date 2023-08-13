package com.enricog.features.routines.ui_components.goal_label

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun GoalText(
    label: GoalLabel,
    modifier: Modifier
) {
    TempoText(
        modifier = modifier,
        text = stringResource(
            id = label.stringResId,
            formatArgs = label.formatArgs.toTypedArray()
        ),
        style = TempoTheme.typography.body2,
    )
}
