package com.enricog.base_test.compose

import androidx.compose.ui.test.junit4.ComposeContentTestRule

operator fun ComposeContentTestRule.invoke(block: ComposeContentTestRule.() -> Unit) {
    block()
}
