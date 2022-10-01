package com.enricog.features.routines.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.ui_components.RoutinesEmptyScene
import com.enricog.features.routines.list.ui_components.RoutinesErrorScene
import com.enricog.features.routines.list.ui_components.RoutinesScene
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.components.toolbar.TempoToolbar
import kotlinx.coroutines.launch

@Composable
internal fun RoutinesScreen(viewModel: RoutinesViewModel) {
    val viewState by viewModel.viewState.collectAsState(RoutinesViewState.Idle)
    val snackbarHostState = rememberSnackbarHostState()

    val scope = rememberCoroutineScope()

    TempoSnackbarHost(state = snackbarHostState) {
        Column(modifier = Modifier.fillMaxSize()) {
            TempoToolbar(title = stringResource(R.string.title_routines))
            viewState.Compose(
                onCreateRoutineClick = {
                    scope.launch { snackbarHostState.show("Lorem ipsum with much longer text to see what happen", "Action") }
                },
                onRoutineClick = viewModel::onRoutineClick,
                onRoutineDelete = viewModel::onRoutineDelete,
                onRetryLoadClick = viewModel::onRetryLoadClick
            )
        }
    }
}

@Composable
internal fun RoutinesViewState.Compose(
    onCreateRoutineClick: () -> Unit,
    onRoutineClick: (Routine) -> Unit,
    onRoutineDelete: (Routine) -> Unit,
    onRetryLoadClick: () -> Unit
) {
    when (this) {
        RoutinesViewState.Idle -> Unit
        RoutinesViewState.Empty ->
            RoutinesEmptyScene(
                onCreateSegmentClick = onCreateRoutineClick
            )
        is RoutinesViewState.Data ->
            RoutinesScene(
                routines = routines,
                onRoutineClick = onRoutineClick,
                onRoutineDelete = onRoutineDelete,
                onCreateRoutineClick = onCreateRoutineClick
            )
        is RoutinesViewState.Error ->
            RoutinesErrorScene(
                throwable = throwable,
                onRetryLoadClick = onRetryLoadClick
            )
    }
}
