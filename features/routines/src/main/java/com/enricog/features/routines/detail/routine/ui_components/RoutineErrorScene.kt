package com.enricog.features.routines.detail.routine.ui_components

import androidx.compose.runtime.Composable
import com.enricog.ui.components.extensions.getLayout

internal const val RoutineErrorSceneTestTag = "RoutineErrorSceneTestTag"

@Composable
internal fun RoutineErrorScene(throwable: Throwable, onRetryLoad: () -> Unit) {
    throwable.getLayout(layoutTestTag = RoutineErrorSceneTestTag, onButtonClick = onRetryLoad)
}