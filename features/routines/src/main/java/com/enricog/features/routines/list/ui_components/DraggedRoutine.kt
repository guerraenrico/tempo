package com.enricog.features.routines.list.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import com.enricog.features.routines.list.models.RoutinesItem.RoutineItem
import com.enricog.ui.theme.TempoTheme

internal const val DraggedRoutineTestTag = "DraggedRoutineTestTag"

@Composable
internal fun DraggedRoutine(
    routine: RoutineItem,
    offsetProvider: () -> Float,
    modifier: Modifier = Modifier
) {
    RoutineItem(
        routineItem = routine,
        onClick = {},
        onDelete = {},
        enableClick = false,
        modifier = modifier
            .testTag(DraggedRoutineTestTag)
            .fillMaxWidth()
            .graphicsLayer {
                translationY = offsetProvider()
                shadowElevation = 50f
            }
            .zIndex(9999f)
            .clip(TempoTheme.shapes.listItem)
    )
}