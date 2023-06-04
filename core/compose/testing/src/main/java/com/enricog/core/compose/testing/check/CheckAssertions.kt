package com.enricog.core.compose.testing.check

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

fun SemanticsNodeInteraction.assertChecked(checked: Boolean): SemanticsNodeInteraction =
    assert(matcher = isChecked(checked = checked))