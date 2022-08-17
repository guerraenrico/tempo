package com.enricog.features.routines.list.ui_components

import androidx.compose.runtime.Composable
import com.enricog.ui.components.extensions.getLayout

internal const val RoutinesErrorSceneTestTag = "RoutinesErrorSceneTestTag"

@Composable
internal fun RoutinesErrorScene(throwable: Throwable, onRetryLoadClick: () -> Unit) {
    throwable.getLayout(layoutTestTag = RoutinesErrorSceneTestTag, onButtonClick = onRetryLoadClick)
}