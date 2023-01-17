package com.enricog.ui.components.icon

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
enum class TempoIconSize(internal val value: Dp) {
    Small(value = 12.dp),
    Normal(value = 14.dp),
    Large(value = 18.dp),
    ExtraLarge(value = 22.dp),

    Original(value = Dp.Unspecified)
}
