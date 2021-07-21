package com.enricog.ui_components.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions internal constructor(
    val spaceXS: Dp,
    val spaceS: Dp,
    val spaceM: Dp,
    val spaceL: Dp,
    val spaceXL: Dp,
    val spaceXXL: Dp
)

internal val defaultDimensions = Dimensions(
    spaceXS = 6.dp,
    spaceS = 10.dp,
    spaceM = 16.dp,
    spaceL = 20.dp,
    spaceXL = 30.dp,
    spaceXXL = 40.dp
)

internal val LocalDimensions = compositionLocalOf { defaultDimensions }
