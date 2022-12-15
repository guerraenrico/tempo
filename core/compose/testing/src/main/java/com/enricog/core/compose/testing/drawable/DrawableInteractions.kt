package com.enricog.core.compose.testing.drawable

import androidx.annotation.DrawableRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

fun SemanticsNodeInteraction.assertDrawable(@DrawableRes id: Int): SemanticsNodeInteraction =
    assert(matcher = hasDrawable(id = id))
