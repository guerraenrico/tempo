package com.enricog.features.timer.ui_components

import androidx.compose.runtime.Composable
import com.enricog.ui.components.extensions.getLayout

internal const val TimerErrorSceneTestTag = "TimerErrorSceneTestTag"

@Composable
internal fun TimerErrorScene(throwable: Throwable, onRetryLoadClick: () -> Unit) {
    throwable.getLayout(layoutTestTag = TimerErrorSceneTestTag, onButtonClick = onRetryLoadClick)
}