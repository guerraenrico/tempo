package com.enricog.features.timer.ui_components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.models.TimerViewState.Counting.BackgroundColor
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.contentColorFor

internal const val TimerCountingSceneTestTag = "TimerCountingSceneTestTag"
internal const val StepTitleTestTag = "StepTitleTestTag"
internal const val SegmentNameTestTag = "SegmentNameTestTag"

@Composable
internal fun TimerCountingScene(
    state: TimerViewState.Counting,
    onToggleTimer: () -> Unit,
    onRestartSegment: () -> Unit
) {
    val orientation = ScreenConfiguration.orientation

    TimerBackground(
        modifier = Modifier.testTag(TimerCountingSceneTestTag),
        clockBackgroundColor = state.clockBackgroundColor
    ) {
        ConstraintLayout(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxSize()
        ) {
            val (title, clock, actionBar) = createRefs()

            Title(
                modifier = Modifier
                    .padding(
                        start = TempoTheme.dimensions.spaceXL,
                        end = TempoTheme.dimensions.spaceXL
                    )
                    .constrainAs(title) {
                        when (orientation) {
                            PORTRAIT -> {
                                top.linkTo(parent.top)
                                bottom.linkTo(clock.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            LANDSCAPE -> {
                                top.linkTo(parent.top)
                                bottom.linkTo(clock.top)
                                start.linkTo(parent.start)
                                end.linkTo(actionBar.start)
                            }
                        }
                        width = Dimension.fillToConstraints
                    },
                stepTitle = stringResource(state.stepTitleId),
                segmentName = state.segmentName
            )

            Clock(
                timeInSeconds = state.timeInSeconds,
                textColor = TempoTheme.colors.contentColorFor(
                    backgroundColor = state.clockBackgroundColor.background
                ),
                modifier = Modifier
                    .constrainAs(clock) {
                        when (orientation) {
                            PORTRAIT -> {
                                top.linkTo(title.bottom)
                                bottom.linkTo(actionBar.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            LANDSCAPE -> {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(actionBar.start)
                            }
                        }
                    }
            )

            ActionsBar(
                timerActions = state.timerActions,
                onStartStopButtonClick = onToggleTimer,
                onRestartSegmentButtonClick = onRestartSegment,
                modifier = Modifier
                    .constrainAs(actionBar) {
                        when (orientation) {
                            PORTRAIT -> {
                                top.linkTo(clock.bottom)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            LANDSCAPE -> {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                        }
                    }
            )
        }
    }
}

@Composable
private fun TimerBackground(
    clockBackgroundColor: BackgroundColor,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val circleTransitionSize = if (orientation == ORIENTATION_PORTRAIT) {
        configuration.screenHeightDp.dp.toPx()
    } else {
        configuration.screenWidthDp.dp.toPx()
    }

    val circleRadius = remember { Animatable(initialValue = 0f) }
    val isCircleAnimating = clockBackgroundColor.ripple != null
    LaunchedEffect(isCircleAnimating) {
        circleRadius.animateTo(
            targetValue = if (isCircleAnimating) circleTransitionSize else 0f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 500)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = clockBackgroundColor.background)
                drawCircle(
                    color = clockBackgroundColor.ripple ?: Color.Transparent,
                    radius = circleRadius.value
                )
            },
        content = content
    )
}

@Composable
private fun Title(
    stepTitle: String,
    segmentName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TempoText(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SegmentNameTestTag),
            text = segmentName,
            textAlign = TextAlign.Center,
            style = TempoTheme.typography.h1.copy(
                fontSize = 35.sp,
                color = TempoTheme.colors.background
            ),
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(20.dp))
        TempoText(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(StepTitleTestTag),
            text = stepTitle,
            textAlign = TextAlign.Center,
            style = TempoTheme.typography.h1.copy(
                fontSize = 25.sp,
                color = TempoTheme.colors.background
            )
        )
    }
}
