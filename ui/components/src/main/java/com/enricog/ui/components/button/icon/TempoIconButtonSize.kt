package com.enricog.ui.components.button.icon

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enricog.ui.components.icon.TempoIconSize

@Immutable
enum class TempoIconButtonSize(
    val box: Dp,
    val icon: TempoIconSize,
    val shadow: Dp
) {
    Small(box = 48.dp, icon = TempoIconSize.Small, shadow = 2.dp),
    Normal(box = 55.dp, icon = TempoIconSize.Normal, shadow = 4.dp),
    Large(box = 65.dp, icon = TempoIconSize.Large, shadow = 8.dp)
}
