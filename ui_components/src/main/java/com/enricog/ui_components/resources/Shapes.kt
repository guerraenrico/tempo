package com.enricog.ui_components.resources

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableContract
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.unit.dp

@Immutable
data class CommonShapes(
    val listItem: RoundedCornerShape
)

internal val defaultCommonShapes = CommonShapes(
    listItem = RoundedCornerShape(10.dp)
)

internal val AmbientCommonShapes = staticAmbientOf { defaultCommonShapes }

@Suppress("unused")
val MaterialTheme.commonShapes: CommonShapes
    @Composable
    @ComposableContract(readonly = true)
    get() = AmbientCommonShapes.current
