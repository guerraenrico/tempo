package com.enricog.ui.components.extensions

import androidx.compose.runtime.Composable
import com.enricog.ui.components.layout.error.TempoLayoutGenericError

@Composable
fun Throwable.getLayout(
    layoutTestTag: String,
    onButtonClick: () -> Unit
) {
    TempoLayoutGenericError(layoutTestTag = layoutTestTag, onButtonClick = onButtonClick)
}
