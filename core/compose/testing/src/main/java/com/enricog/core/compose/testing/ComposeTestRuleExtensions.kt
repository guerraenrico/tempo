package com.enricog.core.compose.testing

import androidx.compose.ui.test.junit4.ComposeContentTestRule

operator fun ComposeContentTestRule.invoke(block: ComposeContentTestRule.() -> Unit) {
    block()
}