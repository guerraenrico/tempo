package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
internal const val ButtonBackTestTag = "ButtonBackTestTag"
internal const val ButtonNextTestTag = "ButtonNextTestTag"

@Composable
internal fun ActionsBar(
    timerActions: Actions,
    onPlayButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (ScreenConfiguration.orientation) {
        PORTRAIT -> ActionsBarPortrait(
            timerActions = timerActions,
            onPlayButtonClick = onPlayButtonClick,
            onBackButtonClick = onBackButtonClick,
            onNextButtonClick = onNextButtonClick,
            modifier = modifier
        )
        LANDSCAPE -> ActionsBarLandscape(
            timerActions = timerActions,
            onPlayButtonClick = onPlayButtonClick,
            onBackButtonClick = onBackButtonClick,
            onNextButtonClick = onNextButtonClick,
            modifier = modifier
        )
    }
}

@Composable
private fun ActionsBarPortrait(
    timerActions: Actions,
    onPlayButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Container(
        modifier = modifier
            .height(TempoIconButtonSize.Large.box)
            .fillMaxWidth()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (backButton, spacer1, spacer2, playButton, nextButton) = createRefs()
            createHorizontalChain(
                backButton,
                spacer1,
                playButton,
                spacer2,
                nextButton,
                chainStyle = ChainStyle.Packed
            )

            BackButton(
                backButton = timerActions.back,
                onClick = onBackButtonClick,
                modifier = Modifier.constrainAs(backButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(spacer1.start)
                }
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .constrainAs(spacer1) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(backButton.end)
                        end.linkTo(playButton.start)
                    }
            )
            PlayButton(
                playButton = timerActions.play,
                onClick = onPlayButtonClick,
                modifier = Modifier.constrainAs(playButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(spacer1.end)
                    end.linkTo(spacer2.start)
                }
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .constrainAs(spacer2) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(playButton.end)
                        end.linkTo(nextButton.start)
                    }
            )
            NextButton(
                nextButton = timerActions.next,
                onClick = onNextButtonClick,
                modifier = Modifier.constrainAs(nextButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(spacer2.end)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
private fun ActionsBarLandscape(
    timerActions: Actions,
    onPlayButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onNextButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Container(
        modifier = modifier
            .width(TempoIconButtonSize.Large.box)
            .fillMaxHeight()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (backButton, spacer1, spacer2, playButton, nextButton) = createRefs()
            createVerticalChain(
                backButton,
                spacer1,
                playButton,
                spacer2,
                nextButton,
                chainStyle = ChainStyle.Packed
            )

            BackButton(
                backButton = timerActions.back,
                onClick = onBackButtonClick,
                modifier = Modifier.constrainAs(backButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(spacer1.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .constrainAs(spacer1) {
                        top.linkTo(backButton.bottom)
                        bottom.linkTo(playButton.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            PlayButton(
                playButton = timerActions.play,
                onClick = onPlayButtonClick,
                modifier = Modifier.constrainAs(playButton) {
                    top.linkTo(spacer1.bottom)
                    bottom.linkTo(spacer2.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .constrainAs(spacer2) {
                        top.linkTo(playButton.bottom)
                        bottom.linkTo(nextButton.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            NextButton(
                nextButton = timerActions.next,
                onClick = onNextButtonClick,
                modifier = Modifier.constrainAs(nextButton) {
                    top.linkTo(spacer2.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
private fun Container(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .padding(TempoTheme.dimensions.spaceM)
            .testTag(ActionBarTestTag) then modifier,
        content = content
    )
}

@Composable
private fun BackButton(
    backButton: Actions.Button,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TempoIconButton(
        onClick = onClick,
        iconResId = backButton.iconResId,
        modifier = modifier.testTag(ButtonBackTestTag),
        contentDescription = stringResource(backButton.contentDescriptionResId),
        size = backButton.size
    )
}

@Composable
private fun NextButton(
    nextButton: Actions.Button,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TempoIconButton(
        onClick = onClick,
        iconResId = nextButton.iconResId,
        modifier = modifier.testTag(ButtonNextTestTag),
        contentDescription = stringResource(nextButton.contentDescriptionResId),
        size = nextButton.size
    )
}

@Composable
private fun PlayButton(
    playButton: Actions.Button,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TempoIconButton(
        onClick = onClick,
        iconResId = playButton.iconResId,
        modifier = modifier.testTag(ButtonToggleStartTestTag),
        contentDescription = stringResource(playButton.contentDescriptionResId),
        size = playButton.size
    )
}
