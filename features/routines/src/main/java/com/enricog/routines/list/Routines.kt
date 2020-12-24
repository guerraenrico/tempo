package com.enricog.routines.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.Routine
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.list.ui_components.EmptyScene
import com.enricog.routines.list.ui_components.RoutinesScene

@Composable
internal fun Routines(viewModel: RoutinesViewModel) {
    val viewState = viewModel.viewState.collectAsState(RoutinesViewState.Idle)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        viewState.value.Compose(
            onCreateRoutineClick = viewModel::onCreateRoutineClick,
            onRoutineClick = viewModel::onRoutineClick
        )
    }
}

@Composable
private fun RoutinesViewState.Compose(
    onCreateRoutineClick: () -> Unit,
    onRoutineClick: (Routine) -> Unit
) {
    when (this) {
        RoutinesViewState.Idle -> {
        }
        RoutinesViewState.Empty ->
            EmptyScene(onCreateSegmentClick = onCreateRoutineClick)
        is RoutinesViewState.Data ->
            RoutinesScene(routines = routines, onRoutineClick = onRoutineClick)
    }.exhaustive
}
