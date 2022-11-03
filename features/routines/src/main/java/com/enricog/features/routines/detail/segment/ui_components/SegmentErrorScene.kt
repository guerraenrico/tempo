package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.runtime.Composable
import com.enricog.ui.components.extensions.getLayout

internal const val SegmentErrorSceneTestTag = "SegmentErrorSceneTestTag"

@Composable
internal fun SegmentErrorScene(throwable: Throwable, onRetryLoadClick: () -> Unit) {
    throwable.getLayout(layoutTestTag = SegmentErrorSceneTestTag, onButtonClick = onRetryLoadClick)
}