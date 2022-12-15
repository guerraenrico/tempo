package com.enricog.core.compose.api.modifiers.semantics

import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

@VisibleForTesting
val DrawableId = SemanticsPropertyKey<Int>(name = "DrawableId")

fun SemanticsPropertyReceiver.drawableId(@DrawableRes resId: Int) {
    set(key = DrawableId, value = resId)
}