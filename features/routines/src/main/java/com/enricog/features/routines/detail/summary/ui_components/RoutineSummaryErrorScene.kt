package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.runtime.Composable
import com.enricog.ui.components.extensions.getLayout

internal const val RoutineSummaryErrorSceneTag = "RoutineSummaryErrorSceneTag"

@Composable
fun RoutineSummaryErrorScene(throwable: Throwable, onRetryLoad: () -> Unit) {
    throwable.getLayout(
        layoutTestTag = RoutineSummaryErrorSceneTag,
        onButtonClick = onRetryLoad
    )
}