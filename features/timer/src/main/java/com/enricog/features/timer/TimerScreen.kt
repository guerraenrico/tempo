package com.enricog.features.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.ui_components.TimerCloseDialog
import com.enricog.features.timer.ui_components.TimerCompletedScene
import com.enricog.features.timer.ui_components.TimerCountingScene
import com.enricog.features.timer.ui_components.TimerErrorScene
import com.enricog.features.timer.ui_components.TimerToolbar
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun TimerScreen(viewModel: TimerViewModel) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle(TimerViewState.Idle)
    var showCloseDialog by remember { mutableStateOf(false) }

    MainContainer(viewState = viewState) {
        TimerToolbar(
            modifier = Modifier.zIndex(1f),
            state = viewState,
            onCloseClick = { showCloseDialog = true },
            onSettingsClick = viewModel::onShowSettings
        )

        AnimatedContent(
            targetState = viewState,
            transitionSpec = {
                if (targetState::class.simpleName == initialState::class.simpleName) {
                    EnterTransition.None togetherWith ExitTransition.None
                } else {
                    (
                        fadeIn(animationSpec = tween(durationMillis = 440, delayMillis = 440)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(durationMillis = 440, delayMillis = 440)
                            )
                        ).togetherWith(fadeOut(animationSpec = tween(durationMillis = 440)))
                }
            },
            label = "timerContent"
        ) { targetViewState ->
            targetViewState.Compose(
                onPlay = viewModel::onPlay,
                onBack = viewModel::onBack,
                onNext = viewModel::onNext,
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
private fun MainContainer(
    modifier: Modifier = Modifier,
    viewState: TimerViewState,
    content: @Composable () -> Unit
) {
    val circleTransitionSize = when (ScreenConfiguration.orientation) {
        PORTRAIT -> ScreenConfiguration.height.toPx()
        LANDSCAPE -> ScreenConfiguration.width.toPx()
    }

    val circleRadius = remember { Animatable(initialValue = 0f) }
    val isCircleAnimating = when (viewState) {
        is TimerViewState.Counting -> viewState.clockBackground.ripple != null
        else -> false
    }
    val clockBackground = when (viewState) {
        is TimerViewState.Counting -> viewState.clockBackground
        else -> TimerViewState.Counting.Background(
            background = TempoTheme.colors.background,
            ripple = null
        )
    }

    LaunchedEffect(isCircleAnimating) {
        circleRadius.animateTo(
            targetValue = if (isCircleAnimating) circleTransitionSize else 0f,
            animationSpec = tween(durationMillis = 1000, delayMillis = 500)
        )
    }

    when (ScreenConfiguration.orientation) {
        PORTRAIT -> Column(
            modifier = modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(color = clockBackground.background)
                    drawCircle(
                        color = clockBackground.ripple ?: Color.Transparent,
                        radius = circleRadius.value
                    )
                }
                .windowInsetsPadding(WindowInsets.statusBars),
            content = { content() }
        )

        LANDSCAPE -> Row(
            modifier = modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(color = clockBackground.background)
                    drawCircle(
                        color = clockBackground.ripple ?: Color.Transparent,
                        radius = circleRadius.value
                    )
                }
                .windowInsetsPadding(WindowInsets.statusBars),
            content = { content() }
        )
    }
}

@Composable
internal fun TimerViewState.Compose(
    onPlay: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onReset: () -> Unit,
    onDone: () -> Unit,
    onRetryLoad: () -> Unit
) {
    when (this) {
        TimerViewState.Idle -> Unit
        is TimerViewState.Counting -> TimerCountingScene(
            state = this,
            onPlay = onPlay,
            onBack = onBack,
            onNext = onNext
        )

        is TimerViewState.Completed -> TimerCompletedScene(
            state = this,
            onReset = onReset,
            onDone = onDone
        )

        is TimerViewState.Error -> TimerErrorScene(
            throwable = throwable,
            onRetryLoadClick = onRetryLoad
        )
    }
}
