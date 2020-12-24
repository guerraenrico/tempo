package com.enricog.routines.list.ui_components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.enricog.entities.routines.Routine

@Composable
internal fun RoutineItem(routine: Routine) {
    Text(
        text = routine.name,
        style = MaterialTheme.typography.h1
    )
}