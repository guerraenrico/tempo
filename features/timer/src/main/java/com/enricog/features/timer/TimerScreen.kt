package com.enricog.features.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.ui_components.TimerCloseDialog
import com.enricog.features.timer.ui_components.TimerCompletedScene
import com.enricog.features.timer.ui_components.TimerCountingScene
import com.enricog.features.timer.ui_components.TimerErrorScene
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton

@Composable
internal fun TimerScreen(viewModel: TimerViewModel) {
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    var showCloseDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .zIndex(1f)
        ) {
            TempoIconButton(
                onClick = { showCloseDialog = true },
                icon = painterResource(R.drawable.ic_timer_close),
                color = TempoButtonColor.TransparentPrimary,
                drawShadow = false,
                contentDescription = stringResource(R.string.content_description_button_exit_routine)
            )
        }

        AnimatedContent(
            targetState = viewState,
            transitionSpec = {
                if (targetState::class == initialState::class) {
                    EnterTransition.None with ExitTransition.None
                } else {
                    fadeIn(
                        animationSpec = tween(durationMillis = 440, delayMillis = 440)
                    ) + scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(durationMillis = 440, delayMillis = 440)
                    ) with fadeOut(animationSpec = tween(durationMillis = 440))
                }
            }
        ) { targetViewState ->
            targetViewState.Compose(
                onToggleTimer = viewModel::onToggleTimer,
                onRestartSegment = viewModel::onRestartSegment,
                onReset = viewModel::onReset,
                onDone = viewModel::onDone,
                onRetryLoad = viewModel::onRetryLoad
            )
        }
    }

    if (showCloseDialog) {
        TimerCloseDialog(
            onClose = viewModel::onClose,
            onDismiss = { showCloseDialog = false }
        )
    }
}

@Composable
internal fun TimerViewState.Compose(
    onToggleTimer: () -> Unit,
    onRestartSegment: () -> Unit,
    onReset: () -> Unit,
    onDone: () -> Unit,
    onRetryLoad: () -> Unit
) {
    when (this) {
        TimerViewState.Idle -> Unit
        is TimerViewState.Counting -> TimerCountingScene(
            state = this,
            onToggleTimer = onToggleTimer,
            onRestartSegment = onRestartSegment
        )
        TimerViewState.Completed -> TimerCompletedScene(
            onReset = onReset,
            onDone = onDone
        )
        is TimerViewState.Error -> TimerErrorScene(
            throwable = throwable,
            onRetryLoadClick = onRetryLoad
        )
    }
}
