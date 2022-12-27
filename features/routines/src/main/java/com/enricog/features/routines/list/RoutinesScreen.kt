package com.enricog.features.routines.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.enricog.entities.ID
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.ui_components.RoutinesEmptyScene
import com.enricog.features.routines.list.ui_components.RoutinesErrorScene
import com.enricog.features.routines.list.ui_components.RoutinesScene
import com.enricog.ui.components.layout.scafold.TempoScreenScaffold
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.toolbar.TempoToolbar

@Composable
internal fun RoutinesScreen(viewModel: RoutinesViewModel) {
    val viewState by viewModel.viewState.collectAsState(RoutinesViewState.Idle)

    TempoScreenScaffold {
        TempoToolbar(title = stringResource(R.string.title_routines))
        viewState.Compose(
            onCreateRoutine = viewModel::onCreateRoutine,
            onRoutine = viewModel::onRoutine,
            onRoutineDelete = viewModel::onRoutineDelete,
            onRoutineMoved = viewModel::onRoutineMoved,
            onRetryLoad = viewModel::onRetryLoad,
            onSnackbarEvent = viewModel::onSnackbarEvent
        )
    }
}

@Composable
internal fun RoutinesViewState.Compose(
    onCreateRoutine: () -> Unit,
    onRoutine: (ID) -> Unit,
    onRoutineDelete: (ID) -> Unit,
    onRoutineMoved: (ID, ID?) -> Unit,
    onRetryLoad: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    when (this) {
        RoutinesViewState.Idle -> Unit
        RoutinesViewState.Empty ->
            RoutinesEmptyScene(
                onCreateSegment = onCreateRoutine
            )
        is RoutinesViewState.Data ->
            RoutinesScene(
                routinesItems = routinesItems,
                message = message,
                onRoutine = onRoutine,
                onRoutineDelete = onRoutineDelete,
                onCreateRoutine = onCreateRoutine,
                onRoutineMoved = onRoutineMoved,
                onSnackbarEvent = onSnackbarEvent
            )
        is RoutinesViewState.Error ->
            RoutinesErrorScene(
                throwable = throwable,
                onRetryLoad = onRetryLoad
            )
    }
}
