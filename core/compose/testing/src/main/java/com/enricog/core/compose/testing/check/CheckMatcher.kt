package com.enricog.core.compose.testing.check

import android.annotation.SuppressLint
import androidx.compose.ui.test.SemanticsMatcher
import com.enricog.core.compose.api.modifiers.semantics.CheckedId

@SuppressLint("VisibleForTests")
fun isChecked(checked: Boolean): SemanticsMatcher =
    SemanticsMatcher.expectValue(key = CheckedId, expectedValue = if (checked) 1 else 0)
