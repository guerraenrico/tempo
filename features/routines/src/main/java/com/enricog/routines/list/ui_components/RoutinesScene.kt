package com.enricog.routines.list.ui_components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Routine

internal const val RoutineSceneTestTag = "RoutineSceneTestTag"

@Composable
internal fun RoutinesScene(routines: List<Routine>, onRoutineClick: (Routine) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSceneTestTag)
    ) {
        items(routines) { item ->
            RoutineItem(item, onRoutineClick)
        }
    }

}