package com.enricog.routines.detail.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.routines.detail.routine.models.RoutineViewState
import com.enricog.routines.detail.routine.ui_components.RoutineFormScene
import com.enricog.ui_components.ambients.navViewModel

@Composable
internal fun RoutineScreen(routineId: Long, viewModel: RoutineViewModel = navViewModel()) {
    onActive {
        viewModel.load(routineId)
    }
    val viewState by viewModel.viewState.collectAsState(RoutineViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        with(viewState) {
            when (this) {
                RoutineViewState.Idle -> {
                }
                is RoutineViewState.Data -> {
                    RoutineFormScene(
                        routine = routine,
                        errors = errors,
                        onRoutineNameChange = viewModel::onRoutineNameTextChange,
                        onStartTimeOffsetChange = viewModel::onRoutineStartTimeOffsetChange,
                        onRoutineBack = viewModel::onRoutineBack
                    )
                }
            }.exhaustive
        }

    }
}