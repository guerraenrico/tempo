package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.features.timer.models.TimerViewState.Counting.Actions
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.theme.TempoTheme

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
    val orientation = ScreenConfiguration.orientation

    ConstraintLayout(
        modifier = modifier
            .padding(TempoTheme.dimensions.spaceM)
            .testTag(ActionBarTestTag) then
                when (orientation) {
                    PORTRAIT -> Modifier
                        .height(TempoIconButtonSize.Large.box)
                        .fillMaxWidth()
                    LANDSCAPE -> Modifier
                        .width(TempoIconButtonSize.Large.box)
                        .fillMaxHeight()
                }
    ) {
        val (restartButton, spacer, toggleStartButton) = createRefs()
        when (orientation) {
            PORTRAIT -> {
                createHorizontalChain(
                    restartButton,
                    spacer,
                    toggleStartButton,
                    chainStyle = ChainStyle.Packed
                )
            }
            LANDSCAPE -> {
                createVerticalChain(
                    restartButton,
                    spacer,
                    toggleStartButton,
                    chainStyle = ChainStyle.Packed
                )
            }
        }

        RestartButton(
            restartButton = timerActions.restart,
            onClick = onRestartSegmentButtonClick,
            modifier = Modifier.constrainAs(restartButton) {
                when (orientation) {
                    PORTRAIT -> {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(spacer.start)
                    }
                    LANDSCAPE -> {
                        top.linkTo(parent.top)
                        bottom.linkTo(spacer.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                }
            }
        )
        Spacer(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .constrainAs(spacer) {
                    when (orientation) {
                        PORTRAIT -> {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(restartButton.end)
                            end.linkTo(toggleStartButton.start)
                        }
                        LANDSCAPE -> {
                            top.linkTo(restartButton.bottom)
                            bottom.linkTo(toggleStartButton.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    }
                }
        )
        ToggleStartButton(
            toggleStartButton = timerActions.toggleStart,
            onClick = onStartStopButtonClick,
            modifier = Modifier.constrainAs(toggleStartButton) {
                when (orientation) {
                    PORTRAIT -> {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(spacer.end)
                        end.linkTo(parent.end)
                    }
                    LANDSCAPE -> {
                        top.linkTo(spacer.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                }
            }
        )
    }
}

@Composable
private fun RestartButton(
    restartButton: Actions.Button,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TempoIconButton(
        onClick = onClick,
        iconResId = restartButton.iconResId,
        modifier = modifier.testTag(ButtonRestartTestTag),
        contentDescription = stringResource(restartButton.contentDescriptionResId),
        size = restartButton.size
    )
}

@Composable
private fun ToggleStartButton(
    toggleStartButton: Actions.Button,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TempoIconButton(
        onClick = onClick,
        iconResId = toggleStartButton.iconResId,
        modifier = modifier.testTag(ButtonToggleStartTestTag),
        contentDescription = stringResource(toggleStartButton.contentDescriptionResId),
        size = toggleStartButton.size
    )
}
