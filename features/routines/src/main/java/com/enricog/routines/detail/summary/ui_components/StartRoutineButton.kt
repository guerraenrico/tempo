package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.resources.TempoTheme

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