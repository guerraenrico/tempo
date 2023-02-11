package com.enricog.features.routines.detail.routine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.ui_components.RoutineErrorScene
import com.enricog.features.routines.detail.routine.ui_components.RoutineFormScene
import com.enricog.ui.components.layout.scafold.TempoScreenScaffold
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.toolbar.TempoToolbar

@Composable
internal fun RoutineScreen(viewModel: RoutineViewModel) {
    val viewState by viewModel.viewState.collectAsState(RoutineViewState.Idle)
    val fieldInputs = viewModel.fieldInputs
    TempoScreenScaffold {
        TempoToolbar(onBack = viewModel::onRoutineBack)
        viewState.Compose(
            inputs = fieldInputs,
            onRoutineNameChange = viewModel::onRoutineNameTextChange,
            onStartTimeOffsetChange = viewModel::onRoutineStartTimeOffsetChange,
            onStartTimeInfo = viewModel::onRoutineStartTimeInfo,
            onRoutineSave = viewModel::onRoutineSave,
            onRetryLoad = viewModel::onRetryLoad,
            onSnackbarEvent = viewModel::onSnackbarEvent
        )
    }
}

@Composable
internal fun RoutineViewState.Compose(
    inputs: RoutineInputs,
    onRoutineNameChange: (TextFieldValue) -> Unit,
    onStartTimeOffsetChange: (TimeText) -> Unit,
    onStartTimeInfo: () -> Unit,
    onRoutineSave: () -> Unit,
    onRetryLoad: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    when (this) {
        RoutineViewState.Idle -> Unit
        is RoutineViewState.Data -> RoutineFormScene(
            inputs = inputs,
            errors = errors,
            message = message,
            onRoutineNameChange = onRoutineNameChange,
            onStartTimeOffsetChange = onStartTimeOffsetChange,
            onStartTimeInfo = onStartTimeInfo,
            onRoutineSave = onRoutineSave,
            onSnackbarEvent = onSnackbarEvent
        )
        is RoutineViewState.Error -> RoutineErrorScene(
            throwable = throwable,
            onRetryLoad = onRetryLoad
        )
    }
}
