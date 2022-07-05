package com.enricog.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
object TempoDimensions {
    val spaceXS: Dp = 6.dp
    val spaceS: Dp = 10.dp
    val spaceM: Dp = 16.dp
    val spaceL: Dp = 20.dp
    val spaceXL: Dp = 30.dp
    val spaceXXL: Dp = 40.dp
}

internal val LocalTempoDimensions = compositionLocalOf { TempoDimensions }
