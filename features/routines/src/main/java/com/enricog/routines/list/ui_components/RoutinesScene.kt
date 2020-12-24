package com.enricog.routines.list.ui_components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.enricog.entities.routines.Routine

@Composable
fun RoutinesScene(routines: List<Routine>, onClick: (Routine) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        ) {
        items(routines) { item ->
            RoutineItem(item)
        }
    }

}