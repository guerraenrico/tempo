package com.enricog.routines.list.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Routine

internal const val RoutineItemTestTag = "RoutineItemTestTag"

@Composable
internal fun RoutineItem(routine: Routine, onClick: (Routine) -> Unit) {
    Surface(
        modifier = Modifier
            .testTag(RoutineItemTestTag)
            .clickable { onClick(routine) }
    ) {
        Text(
            text = routine.name,
            style = MaterialTheme.typography.h1
        )
    }
}