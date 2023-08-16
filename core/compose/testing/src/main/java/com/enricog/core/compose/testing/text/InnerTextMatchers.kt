package com.enricog.core.compose.testing.text

import android.annotation.SuppressLint
import androidx.compose.ui.test.SemanticsMatcher
import com.enricog.core.compose.api.modifiers.semantics.InnerText

@SuppressLint("VisibleForTests")
fun hasInnerTextEquals(value: String): SemanticsMatcher =
    SemanticsMatcher.expectValue(key = InnerText, expectedValue = value)
