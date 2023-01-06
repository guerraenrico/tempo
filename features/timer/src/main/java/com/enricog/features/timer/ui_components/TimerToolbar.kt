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

    ConstraintLayout(
        modifier = when (orientation) {
            PORTRAIT -> modifier.fillMaxWidth()
            LANDSCAPE -> modifier.fillMaxHeight()
        }
    ) {
        val (closeButton, soundButton) = createRefs()

        TempoIconButton(
            modifier = Modifier
                .testTag(TimerToolbarCloseButtonTestTag)
                .constrainAs(closeButton) {
                    top.linkTo(parent.top)
                    when (orientation) {
                        PORTRAIT -> bottom.linkTo(parent.bottom)
                        LANDSCAPE -> end.linkTo(parent.end)
                    }
                    start.linkTo(parent.start)
                },
            onClick = onCloseClick,
            iconResId = R.drawable.ic_timer_close,
            color = buttonColor,
            drawShadow = false,
            contentDescription = stringResource(R.string.content_description_button_exit_routine)
        )

        if (state is TimerViewState.Counting) {
            TempoIconButton(
                modifier = Modifier
                    .testTag(TimerToolbarSoundButtonTestTag)
                    .constrainAs(soundButton) {
                        when (orientation) {
                            PORTRAIT -> top.linkTo(parent.top)
                            LANDSCAPE -> start.linkTo(parent.start)
                        }
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                onClick = onSoundClick,
                iconResId = if (state.isSoundEnabled) R.drawable.ic_timer_sound_enabled else R.drawable.ic_timer_sound_disabled,
                color = buttonColor,
                drawShadow = false,
                contentDescription = stringResource(R.string.content_description_button_toggle_sound)
            )
        }
    }
}
