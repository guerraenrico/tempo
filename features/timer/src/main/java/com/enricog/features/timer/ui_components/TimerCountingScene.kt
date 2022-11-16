package com.enricog.features.timer.ui_components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.dialog.TempoDialogAction
import com.enricog.ui.components.dialog.TempoDialogAlert
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val TimerCountingSceneTestTag = "TimerCountingSceneTestTag"
internal const val StepTitleTestTag = "StepTitleTestTag"
internal const val SegmentNameTestTag = "SegmentNameTestTag"

@Composable
internal fun TimerCountingScene(
    state: TimerViewState.Counting,
    onToggleTimer: () -> Unit,
    onRestartSegment: () -> Unit,
    onReset: () -> Unit,
    onDone: () -> Unit,
    onClose: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val orientation = configuration.orientation

    val count = state.step.count

    val alpha by updateTransition(targetState = state.isRoutineCompleted, label = "alpha")
        .animateFloat(label = "alpha") { if (it) 0f else 1f }

    val circleScale by updateTransition(
        targetState = state.clockBackgroundColor.ripple != null,
        label = "circleScale"
    ).animateFloat(
        transitionSpec = { tween(durationMillis = 1000, delayMillis = 500) },
        label = "circleScale"
    ) { if (it) screenHeight.toPx() else 0f }

    var dialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = state.clockBackgroundColor.foreground)
                if (state.clockBackgroundColor.ripple != null) {
                    drawCircle(
                        color = state.clockBackgroundColor.ripple,
                        radius = circleScale
                    )
                }
            }
            .testTag(TimerCountingSceneTestTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TempoIconButton(
                onClick = { dialogOpen = true },
                icon = painterResource(R.drawable.ic_timer_close),
                color = TempoButtonColor.TransparentPrimary,
                drawShadow = false,
                contentDescription = stringResource(R.string.content_description_button_exit_routine)
            )
        }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (title, clock, actionBar) = createRefs()

            Title(
                modifier = Modifier
                    .alpha(alpha)
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
                seconds = count.seconds,
                modifier = Modifier
                    .constrainAs(clock) {
                        if (orientation == ORIENTATION_PORTRAIT) {
                            top.linkTo(title.bottom)
                        } else {
                            top.linkTo(parent.top)
                        }
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(actionBar.top)
                    }
            )

            ActionsBar(
                isTimeRunning = count.isRunning,
                isRoutineCompleted = state.isRoutineCompleted,
                onStartStopButtonClick = onToggleTimer,
                onRestartSegmentButtonClick = onRestartSegment,
                onResetButtonClick = onReset,
                onDoneButtonClick = onDone,
                modifier = Modifier
                    .constrainAs(actionBar) {
                        top.linkTo(clock.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }

    if (dialogOpen) {
        TempoDialogAlert(
            title = stringResource(R.string.dialog_exit_time_title),
            description = stringResource(R.string.dialog_exit_time_description),
            positiveAction = TempoDialogAction(
                text = stringResource(R.string.dialog_exit_time_action_positive),
                onClick = onClose,
                contentDescription = stringResource(R.string.content_description_dialog_quite_routine_button_positive)
            ),
            negativeAction = TempoDialogAction(
                text = stringResource(R.string.dialog_exit_time_action_negative),
                onClick = { dialogOpen = false },
                contentDescription = stringResource(R.string.content_description_dialog_quite_routine_button_negative)
            ),
            onDismiss = { dialogOpen = false },
            isCancellable = true
        )
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
        Spacer(modifier = Modifier.height(20.dp))
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
