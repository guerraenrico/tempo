package com.enricog.routines.detail.routine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.Seconds
import com.enricog.routines.detail.routine.models.RoutineViewState
import com.enricog.routines.detail.routine.ui_components.RoutineFormScene
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun RoutineScreen(viewModel: RoutineViewModel) {
    val viewState by viewModel.viewState.collectAsState(RoutineViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TempoToolbar(onBack = viewModel::onRoutineBack)
        viewState.Compose(
            onRoutineNameChange = viewModel::onRoutineNameTextChange,
            onStartTimeOffsetChange = viewModel::onRoutineStartTimeOffsetChange,
            onRoutineSave = viewModel::onRoutineSave
        )
    }
}

@Composable
internal fun RoutineViewState.Compose(
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Seconds) -> Unit,
    onRoutineSave: () -> Unit
) {
    when (this) {
        RoutineViewState.Idle -> {
        }
        is RoutineViewState.Data -> {
            RoutineFormScene(
                routine = routine,
                errors = errors,
                onRoutineNameChange = onRoutineNameChange,
                onStartTimeOffsetChange = onStartTimeOffsetChange,
                onRoutineSave = onRoutineSave
            )
        }
    }.exhaustive
}
