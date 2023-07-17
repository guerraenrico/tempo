package com.enricog.features.timer.ui_components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.Background
import com.enricog.features.timer.models.TimerViewState.Counting.RoundText
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val TimerCountingSceneTestTag = "TimerCountingSceneTestTag"
internal const val StepTitleTestTag = "StepTitleTestTag"
internal const val SegmentNameTestTag = "SegmentNameTestTag"
internal const val SegmentRoundTestTag = "SegmentRoundTestTag"
internal const val RoutineRoundTestTag = "RoutineRoundTestTag"

@Composable
internal fun TimerCountingScene(
    state: TimerViewState.Counting,
    onPlay: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    when (ScreenConfiguration.orientation) {
        PORTRAIT -> TimerCountingScenePortrait(
            state = state,
            onPlay = onPlay,
            onBack = onBack,
            onNext = onNext
        )

        LANDSCAPE -> TimerCountingSceneLandscape(
            state = state,
            onPlay = onPlay,
            onBack = onBack,
            onNext = onNext
        )
    }
}

@Composable
private fun TimerCountingScenePortrait(
    state: TimerViewState.Counting,
    onPlay: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    TimerBackground(clockBackground = state.clockBackground) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (rounds, title, clock, actionBar) = createRefs()

            if (state.hasRoundsText) {
                Column(
                    modifier = Modifier
                        .constrainAs(rounds) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                        .padding(horizontal = TempoTheme.dimensions.spaceM)
                ) {
                    if (state.routineRoundText != null) {
                        RoundText(
                            modifier = Modifier
                                .testTag(RoutineRoundTestTag)
                                .fillMaxWidth(),
                            roundText = state.routineRoundText,
                            textAlign = TextAlign.Center,
                            textColor = state.clockOnBackgroundColor
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TempoTheme.dimensions.spaceM)
                    .constrainAs(title) {
                        if (state.hasRoundsText) {
                            top.linkTo(rounds.bottom)
                        } else {
                            top.linkTo(parent.top)
                        }
                        bottom.linkTo(clock.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.segmentRoundText != null) {
                    RoundText(
                        modifier = Modifier
                            .testTag(SegmentRoundTestTag)
                            .fillMaxWidth(),
                        roundText = state.segmentRoundText,
                        textAlign = TextAlign.Center,
                        textColor = state.clockOnBackgroundColor
                    )
                }
                SegmentName(
                    segmentName = state.segmentName,
                    textColor = state.clockOnBackgroundColor,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceL))
                StepTitleText(
                    stepTitle = stringResource(id = state.stepTitleId),
                    textColor = state.clockOnBackgroundColor
                )
            }

            Clock(
                timeInSeconds = state.timeInSeconds,
                textColor = state.clockOnBackgroundColor,
                modifier = Modifier.constrainAs(clock) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(actionBar.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            ActionsBar(
                timerActions = state.timerActions,
                onPlayButtonClick = onPlay,
                onBackButtonClick = onBack,
                onNextButtonClick = onNext,
                modifier = Modifier.constrainAs(actionBar) {
                    top.linkTo(clock.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
private fun TimerCountingSceneLandscape(
    state: TimerViewState.Counting,
    onPlay: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    TimerBackground(clockBackground = state.clockBackground) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (rounds, title, clock, actionBar) = createRefs()

            if (state.hasRoundsText) {
                Row(
                    modifier = Modifier
                        .constrainAs(rounds) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(actionBar.start)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                        .padding(horizontal = TempoTheme.dimensions.spaceM)
                ) {
                    if (state.routineRoundText != null) {
                        RoundText(
                            modifier = Modifier
                                .testTag(RoutineRoundTestTag)
                                .weight(0.5f),
                            roundText = state.routineRoundText,
                            textAlign = TextAlign.Start,
                            textColor = state.clockOnBackgroundColor
                        )
                    }
                    if (state.segmentRoundText != null) {
                        RoundText(
                            modifier = Modifier
                                .testTag(SegmentRoundTestTag)
                                .weight(0.5f),
                            roundText = state.segmentRoundText,
                            textAlign = TextAlign.End,
                            textColor = state.clockOnBackgroundColor
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TempoTheme.dimensions.spaceM)
                    .constrainAs(title) {
                        if (state.hasRoundsText) {
                            top.linkTo(rounds.bottom)
                        } else {
                            top.linkTo(parent.top)
                        }
                        bottom.linkTo(clock.top)
                        start.linkTo(parent.start)
                        end.linkTo(actionBar.start)
                        width = Dimension.fillToConstraints
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SegmentName(
                    segmentName = state.segmentName,
                    textColor = state.clockOnBackgroundColor,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceL))
                StepTitleText(
                    stepTitle = stringResource(id = state.stepTitleId),
                    textColor = state.clockOnBackgroundColor
                )
            }

            Clock(
                timeInSeconds = state.timeInSeconds,
                textColor = state.clockOnBackgroundColor,
                modifier = Modifier.constrainAs(clock) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(actionBar.start)
                }
            )
            ActionsBar(
                timerActions = state.timerActions,
                onPlayButtonClick = onPlay,
                onBackButtonClick = onBack,
                onNextButtonClick = onNext,
                modifier = Modifier.constrainAs(actionBar) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
private fun TimerBackground(
    clockBackground: Background,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val circleTransitionSize = when (ScreenConfiguration.orientation) {
        PORTRAIT -> ScreenConfiguration.height.toPx()
        LANDSCAPE -> ScreenConfiguration.width.toPx()
    }

    val circleRadius = remember { Animatable(initialValue = 0f) }
    val isCircleAnimating = clockBackground.ripple != null
    LaunchedEffect(isCircleAnimating) {
        circleRadius.animateTo(
            targetValue = if (isCircleAnimating) circleTransitionSize else 0f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 500)
        )
    }

    Box(
        modifier = modifier
            .testTag(TimerCountingSceneTestTag)
            .fillMaxSize()
            .drawBehind {
                drawRect(color = clockBackground.background)
                drawCircle(
                    color = clockBackground.ripple ?: Color.Transparent,
                    radius = circleRadius.value
                )
            },
        content = content
    )
}

@Composable
private fun SegmentName(
    segmentName: String,
    textColor: Color,
    maxLines: Int,
    modifier: Modifier = Modifier
) {
    TempoText(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SegmentNameTestTag)
            .then(modifier),
        text = segmentName,
        textAlign = TextAlign.Center,
        style = TempoTheme.typography.h1.copy(
            fontSize = 35.sp,
            color = textColor
        ),
        maxLines = maxLines
    )
}

@Composable
private fun StepTitleText(
    stepTitle: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    TempoText(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(StepTitleTestTag)
            .then(modifier),
        text = stepTitle,
        textAlign = TextAlign.Center,
        style = TempoTheme.typography.h1.copy(
            fontSize = 25.sp,
            color = textColor
        )
    )
}

@Composable
private fun RoundText(
    roundText: RoundText,
    textColor: Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    TempoText(
        modifier = modifier,
        text = stringResource(
            id = roundText.labelId,
            formatArgs = roundText.formatArgs.toTypedArray()
        ),
        textAlign = textAlign,
        style = TempoTheme.typography.h1.copy(
            fontSize = 20.sp,
            color = textColor
        )
    )
}
