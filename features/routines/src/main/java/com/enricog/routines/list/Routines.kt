package com.enricog.routines.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.list.ui_components.EmptyScene
import com.enricog.routines.list.ui_components.RoutinesScene
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun Routines(navController: NavController, viewModel: RoutinesViewModel = viewModel()) {
    val viewState = viewModel.viewState.collectAsState(RoutinesViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TempoToolbar(title = stringResource(R.string.title_routines))
        viewState.value.Compose(
            onCreateRoutineClick = viewModel::onCreateRoutineClick,
//            onRoutineClick = viewModel::onRoutineClick
            onRoutineClick = { navController.navigate("routines?routineId=1") }
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
            RoutinesScene(
                routines = routines,
                onRoutineClick = onRoutineClick,
                onCreateRoutineClick = onCreateRoutineClick
            )
    }.exhaustive
}
