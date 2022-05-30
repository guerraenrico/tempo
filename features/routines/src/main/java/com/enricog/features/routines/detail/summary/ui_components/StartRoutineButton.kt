package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun StartRoutineButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TempoIconButton(
        modifier = modifier.padding(TempoTheme.dimensions.spaceM),
        onClick = onClick,
        icon = painterResource(R.drawable.ic_routine_play),
        color = TempoButtonColor.Accent,
        size = TempoIconButtonSize.Large,
        contentDescription = stringResource(R.string.content_description_button_start_routine)
    )
}