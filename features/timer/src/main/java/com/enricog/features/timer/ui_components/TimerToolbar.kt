package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.contentColorFor

internal const val TimerToolbarCloseButtonTestTag = "TimerToolbarCloseButtonTestTag"
internal const val TimerToolbarSoundButtonTestTag = "TimerToolbarSoundButtonTestTag"

@Composable
internal fun TimerToolbar(
    state: TimerViewState,
    onCloseClick: () -> Unit,
    onSoundClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val orientation = ScreenConfiguration.orientation

    val buttonColor = when (state) {
        is TimerViewState.Error,
        TimerViewState.Completed,
        TimerViewState.Idle -> TempoButtonColor.TransparentPrimary
        is TimerViewState.Counting -> TempoButtonColor.Adaptive(
            enabledBackgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            enabledContentColor = TempoTheme.colors.contentColorFor(
                backgroundColor = state.clockBackgroundColor.background
            ),
            disabledContentColor = TempoTheme.colors.contentColorFor(
                backgroundColor = state.clockBackgroundColor.background.copy(alpha = .5f)
            )
        )
    }

    when (orientation) {
        PORTRAIT -> TimerToolbarPortrait(
            state = state,
            buttonColor = buttonColor,
            onCloseClick = onCloseClick,
            onSoundClick = onSoundClick,
            modifier = modifier
        )
        LANDSCAPE -> TimerToolbarLandscape(
            state = state,
            buttonColor = buttonColor,
            onCloseClick = onCloseClick,
            onSoundClick = onSoundClick,
            modifier = modifier
        )
    }
}

@Composable
private fun TimerToolbarPortrait(
    state: TimerViewState,
    buttonColor: TempoButtonColor,
    onCloseClick: () -> Unit,
    onSoundClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (closeButton, soundButton) = createRefs()

        CloseButton(
            modifier = Modifier.constrainAs(closeButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            },
            buttonColor = buttonColor,
            onClick = onCloseClick
        )
        SoundButton(
            modifier = Modifier.constrainAs(soundButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            state = state,
            buttonColor = buttonColor,
            onClick = onSoundClick
        )
    }
}

@Composable
private fun TimerToolbarLandscape(
    state: TimerViewState,
    buttonColor: TempoButtonColor,
    onCloseClick: () -> Unit,
    onSoundClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxHeight()) {
        val (closeButton, soundButton) = createRefs()

        CloseButton(
            modifier = Modifier
                .constrainAs(closeButton) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            buttonColor = buttonColor,
            onClick = onCloseClick
        )
        SoundButton(
            modifier = Modifier.constrainAs(soundButton) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            state = state,
            buttonColor = buttonColor,
            onClick = onSoundClick,
        )
    }
}

@Composable
private fun CloseButton(
    buttonColor: TempoButtonColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TempoIconButton(
        modifier = modifier.testTag(TimerToolbarCloseButtonTestTag),
        onClick = onClick,
        iconResId = R.drawable.ic_timer_close,
        color = buttonColor,
        drawShadow = false,
        contentDescription = stringResource(R.string.content_description_button_exit_routine)
    )
}

@Composable
private fun SoundButton(
    state: TimerViewState,
    buttonColor: TempoButtonColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state is TimerViewState.Counting) {
        TempoIconButton(
            modifier = modifier.testTag(TimerToolbarSoundButtonTestTag),
            onClick = onClick,
            iconResId = if (state.isSoundEnabled) R.drawable.ic_timer_sound_enabled else R.drawable.ic_timer_sound_disabled,
            color = buttonColor,
            size = TempoIconButtonSize.Large,
            drawShadow = false,
            contentDescription = stringResource(R.string.content_description_button_toggle_sound)
        )
    }
}
