package com.enricog.ui_components.resources

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
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

@Suppress("unused")
val MaterialTheme.commonShapes: CommonShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalCommonShape.current
