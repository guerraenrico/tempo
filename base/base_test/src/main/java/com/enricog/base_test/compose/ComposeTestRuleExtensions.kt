package com.enricog.base_test.compose

import androidx.compose.ui.test.junit4.ComposeTestRule

operator fun ComposeTestRule.invoke(block: ComposeTestRule.() -> Unit) {
    block()
}
