package com.enricog.routines.list.ui_components

import androidx.compose.animation.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Routine
import com.enricog.ui_components.resources.dimensions
import com.enricog.ui_components.surfaces.ListItemSurface

internal const val RoutineItemTestTag = "RoutineItemTestTag"

@Composable
internal fun RoutineItem(
    routine: Routine,
    onClick: (Routine) -> Unit,
    onDelete: (Routine) -> Unit,
    modifier: Modifier
) {
//    var deleted by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState {
        if (it == DismissValue.DismissedToStart) {
//            deleted = !deleted
            onDelete(routine)
        }
        it == DismissValue.DismissedToStart
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { dismissDirection ->
            FractionalThreshold(if (dismissDirection == DismissDirection.EndToStart) 0.25f else 0.5f)
        },
        background = {
            val color = animate(
                when(dismissState.targetValue) {
                    DismissValue.DismissedToStart -> Color.Red
                    else -> Color.Transparent
                }
            )
            Box(
                modifier = modifier.fillMaxSize().background(color)
            )
        },
        dismissContent = {
            ListItemSurface(
                modifier = modifier
                    .testTag(RoutineItemTestTag),
                onClick = { onClick(routine) }
            ) {
                Text(
                    modifier = Modifier.padding(MaterialTheme.dimensions.spaceM),
                    text = routine.name,
                    style = MaterialTheme.typography.h2
                )
            }
        }
    )

}