package com.enricog.core.compose.testing.drawable

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.ui.test.SemanticsMatcher
import com.enricog.core.compose.api.modifiers.semantics.DrawableId

@SuppressLint("VisibleForTests")
fun hasDrawable(@DrawableRes id: Int): SemanticsMatcher =
    SemanticsMatcher.expectValue(key = DrawableId, expectedValue = id)