package com.enricog.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class CommonShapes(
    val listItem: RoundedCornerShape
)

internal val defaultCommonShapes = CommonShapes(
    listItem = RoundedCornerShape(10.dp)
)

internal val LocalCommonShape = compositionLocalOf { defaultCommonShapes }
