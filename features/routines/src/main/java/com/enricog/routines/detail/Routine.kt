package com.enricog.routines.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
internal fun Routine(navController: NavController, routineId: Long?) {
    Column {
        Text("routineId: $routineId")
        Button(onClick = { navController.navigate("routines")}) {
            Text("navigate")
        }
    }
}