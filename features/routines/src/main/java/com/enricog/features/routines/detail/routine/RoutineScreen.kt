package com.enricog.features.routines.detail.routine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.features.routines.detail.routine.ui_components.RoutineErrorScene
import com.enricog.features.routines.detail.routine.ui_components.RoutineFormScene
import com.enricog.ui.components.dropDown.TempoDropDownItem
import com.enricog.ui.components.layout.scafold.TempoScreenScaffold
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.toolbar.TempoToolbar

@Composable
internal fun RoutineScreen(viewModel: RoutineViewModel) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle(RoutineViewState.Idle)
    val fieldInputs = viewModel.fieldInputs
    TempoScreenScaffold {
        TempoToolbar(onBack = viewModel::onRoutineBack)
        viewState.Compose(
            inputs = fieldInputs,
            onRoutineNameChange = viewModel::onRoutineNameChange,
            onPreparationTimeChange = viewModel::onRoutinePreparationTimeChange,
            onRoutineRoundsChange = viewModel::onRoutineRoundsChange,
            onFrequencyGoalCheck = viewModel::onFrequencyGoalCheck,
            onFrequencyGoalTimesChange = viewModel::onFrequencyGoalTimesChange,
            onFrequencyGoalPeriodChange = viewModel::onFrequencyGoalPeriodChange,
            onPreparationTimeInfo = viewModel::onRoutinePreparationTimeInfo,
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
    onPreparationTimeChange: (TimeText) -> Unit,
    onRoutineRoundsChange: (TextFieldValue) -> Unit,
    onPreparationTimeInfo: () -> Unit,
    onFrequencyGoalCheck: (Boolean) -> Unit,
    onFrequencyGoalTimesChange: (TextFieldValue) -> Unit,
    onFrequencyGoalPeriodChange: (TempoDropDownItem) -> Unit,
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
            frequencyGoalItems = frequencyGoalItems,
            onRoutineNameChange = onRoutineNameChange,
            onPreparationTimeChange = onPreparationTimeChange,
            onRoutineRoundsChange = onRoutineRoundsChange,
            onPreparationTimeInfo = onPreparationTimeInfo,
            onFrequencyGoalCheck = onFrequencyGoalCheck,
            onFrequencyGoalTimesChange = onFrequencyGoalTimesChange,
            onFrequencyGoalPeriodChange = onFrequencyGoalPeriodChange,
            onRoutineSave = onRoutineSave,
            onSnackbarEvent = onSnackbarEvent
        )

        is RoutineViewState.Error -> RoutineErrorScene(
            throwable = throwable,
            onRetryLoad = onRetryLoad
        )
    }
}
