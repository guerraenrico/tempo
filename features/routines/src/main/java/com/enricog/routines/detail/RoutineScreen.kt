package com.enricog.routines.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.routines.detail.models.RoutineViewState
import com.enricog.routines.detail.ui_components.RoutineFormScene
import com.enricog.ui_components.ambients.navViewModel

@Composable
internal fun RoutineScreen(routineId: Long, viewModel: RoutineViewModel = navViewModel()) {
    onActive {
        viewModel.load(routineId)
    }
    val viewState = viewModel.viewState.collectAsState(RoutineViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        viewState.value.Compose(viewModel::onStartRoutine)
    }
}

@Composable
private fun RoutineViewState.Compose(
    onStartRoutine: () -> Unit
) {
    when (this) {
        RoutineViewState.Idle -> {
            Button(onClick = onStartRoutine) {
                Text("navigate")
            }
        }
        is RoutineViewState.Data ->  {
            RoutineFormScene()
        }
    }.exhaustive
}