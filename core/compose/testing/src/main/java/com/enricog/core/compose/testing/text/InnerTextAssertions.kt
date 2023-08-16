package com.enricog.core.compose.testing.text

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

fun SemanticsNodeInteraction.assertInnerTextEquals(value: String): SemanticsNodeInteraction =
    assert(matcher = hasInnerTextEquals(value = value))
