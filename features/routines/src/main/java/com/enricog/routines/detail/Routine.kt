package com.enricog.routines.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.enricog.ui_components.extensions.navViewModel

@Composable
internal fun Routine(routineId: Long?, viewModel: RoutineViewModel = navViewModel()) {
    Column {
        Text("routineId: $routineId")
        Button(onClick = viewModel::onStartRoutine) {
            Text("navigate")
        }
    }
}