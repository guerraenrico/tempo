package com.enricog.features.timer.ui_components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.constraintlayout.compose.ConstraintLayout
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.dialog.TempoDialogAction
import com.enricog.ui.components.dialog.TempoDialogAlert
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val CountingSceneTestTag = "CountingSceneTestTag"
internal const val StepTitleTestTag = "StepTitleTestTag"
internal const val SegmentNameTestTag = "SegmentNameTestTag"

@Composable
internal fun CountingScene(
    state: TimerViewState.Counting,
    onStartStopButtonClick: () -> Unit,
    onRestartSegmentButtonClick: () -> Unit,
    onResetButtonClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val orientation = configuration.orientation
    val minClockSize = 200.dp

    val upOffset = screenHeight / 4
    val clockSize = max(minClockSize, min(screenHeight, screenWidth) / 2)
    val count = state.step.count

    val alpha by updateTransition(targetState = state.isRoutineCompleted, label = "alpha")
        .animateFloat(label = "alpha") { if (it) 0f else 1f }
    val offset by updateTransition(targetState = state.isRoutineCompleted, label = "offset")
        .animateFloat(label = "offset") { if (it) 0f else 1f }
    val scale by updateTransition(targetState = state.isRoutineCompleted, label = "scale")
        .animateFloat(label = "scale") { if (it) 0.5f else 1f }

    val timerOffset = lerp((-upOffset), 0.dp, offset)
    val actionBarOffset = lerp((-upOffset), 0.dp, offset)

    var dialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(CountingSceneTestTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TempoIconButton(
                onClick = { dialogOpen = true },
                icon = painterResource(R.drawable.ic_timer_close),
                drawShadow = false,
                contentDescription = stringResource(R.string.content_description_button_exit_routine)
            )
        }
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
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
                backgroundColor = state.clockBackgroundColor,
                seconds = count.seconds,
                size = clockSize,
                modifier = Modifier
                    .offset(y = timerOffset)
                    .scale(scale)
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
                onStartStopButtonClick = onStartStopButtonClick,
                onRestartSegmentButtonClick = onRestartSegmentButtonClick,
                onResetButtonClick = onResetButtonClick,
                onDoneButtonClick = onDoneButtonClick,
                modifier = Modifier
                    .offset(y = actionBarOffset)
                    .constrainAs(actionBar) {
                        top.linkTo(clock.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

        if (dialogOpen) {
            TempoDialogAlert(
                title = stringResource(R.string.dialog_exit_time_title),
                description = stringResource(R.string.dialog_exit_time_description),
                positiveAction = TempoDialogAction(
                    text = stringResource(R.string.dialog_exit_time_action_positive),
                    onClick = onCloseButtonClick,
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
            text = stepTitle,
            style = TempoTheme.typography.h3,
            modifier = Modifier.testTag(StepTitleTestTag)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TempoText(
            text = segmentName,
            style = TempoTheme.typography.h1.copy(fontWeight = FontWeight.Normal),
            modifier = Modifier.testTag(SegmentNameTestTag)
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}
