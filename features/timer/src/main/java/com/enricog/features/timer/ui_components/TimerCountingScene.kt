package com.enricog.features.timer.ui_components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val TimerCountingSceneTestTag = "TimerCountingSceneTestTag"
internal const val StepTitleTestTag = "StepTitleTestTag"
internal const val SegmentNameTestTag = "SegmentNameTestTag"

@Composable
internal fun TimerCountingScene(
    state: TimerViewState.Counting,
    onToggleTimer: () -> Unit,
    onRestartSegment: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val circleTransitionSize = if (orientation == ORIENTATION_PORTRAIT) {
        configuration.screenHeightDp.dp.toPx()
    } else {
        configuration.screenWidthDp.dp.toPx()
    }

    val circleRadius = remember { Animatable(initialValue = 0f) }
    val isCircleAnimating = state.clockBackgroundColor.ripple != null
    LaunchedEffect(isCircleAnimating) {
        circleRadius.animateTo(
            targetValue = if (isCircleAnimating) circleTransitionSize else 0f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 500)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = state.clockBackgroundColor.foreground)
                drawCircle(
                    color = state.clockBackgroundColor.ripple ?: Color.Transparent,
                    radius = circleRadius.value
                )
            }
            .testTag(TimerCountingSceneTestTag)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (title, clock, actionBar) = createRefs()

            Title(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        if (orientation == ORIENTATION_PORTRAIT) {
                            end.linkTo(parent.end)
                            bottom.linkTo(clock.top)
                        } else {
                            end.linkTo(clock.start)
                            bottom.linkTo(parent.bottom)
                        }
                    },
                stepTitle = stringResource(state.stepTitleId),
                segmentName = state.segmentName
            )

            Clock(
                timeInSeconds = state.timeInSeconds,
                modifier = Modifier
                    .constrainAs(clock) {
                        if (orientation == ORIENTATION_PORTRAIT) {
                            top.linkTo(title.bottom)
                            bottom.linkTo(actionBar.top)
                        } else {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            ActionsBar(
                timerActions = state.timerActions,
                onStartStopButtonClick = onToggleTimer,
                onRestartSegmentButtonClick = onRestartSegment,
                modifier = Modifier
                    .constrainAs(actionBar) {
                        if (orientation == ORIENTATION_PORTRAIT) {
                            top.linkTo(clock.bottom)
                            start.linkTo(parent.start)
                        } else {
                            top.linkTo(parent.top)
                            start.linkTo(clock.end)
                        }
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }


}

@Composable
private fun Title(
    stepTitle: String,
    segmentName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TempoText(
            text = segmentName,
            style = TempoTheme.typography.h1.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 35.sp
            ),
            modifier = Modifier.testTag(SegmentNameTestTag)
        )
        Spacer(modifier = Modifier.height(30.dp))
        TempoText(
            text = stepTitle,
            style = TempoTheme.typography.h1.copy(
                fontSize = 25.sp
            ),
            modifier = Modifier.testTag(StepTitleTestTag)
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}
