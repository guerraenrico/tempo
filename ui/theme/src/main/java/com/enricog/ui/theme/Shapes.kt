package com.enricog.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
object TempoShapes {

    val small: CornerBasedShape = RoundedCornerShape(4.dp)

    val medium: CornerBasedShape = RoundedCornerShape(4.dp)

    val large: CornerBasedShape = RoundedCornerShape(0.dp)

    val listItem: RoundedCornerShape = RoundedCornerShape(10.dp)
}

internal val LocalTempoShapes = compositionLocalOf { TempoShapes }

internal val defaultShapes = Shapes(
    small = TempoShapes.small,
    medium = TempoShapes.medium,
    large = TempoShapes.large
)
