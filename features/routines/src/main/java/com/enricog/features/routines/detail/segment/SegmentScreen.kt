package com.enricog.features.routines.detail.segment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.ui_components.SegmentErrorScene
import com.enricog.features.routines.detail.segment.ui_components.SegmentFormScene
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.components.layout.scafold.TempoScreenScaffold
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.toolbar.TempoToolbar

@Composable
internal fun SegmentScreen(viewModel: SegmentViewModel) {
    val viewState by viewModel.viewState.collectAsState(SegmentViewState.Idle)
    TempoScreenScaffold {
        TempoToolbar(onBack = viewModel::onSegmentBack)

        viewState.Compose(
            onSegmentNameChange = viewModel::onSegmentNameTextChange,
            onSegmentTimeChange = viewModel::onSegmentTimeChange,
            onSegmentTimeTypeChange = viewModel::onSegmentTypeChange,
            onSegmentSave = viewModel::onSegmentSave,
            onRetryLoad = viewModel::onRetryLoad,
            onSnackbarEvent = viewModel::onSnackbarEvent
        )
    }
}

@Composable
internal fun SegmentViewState.Compose(
    onSegmentNameChange: (TextFieldValue) -> Unit,
    onSegmentTimeChange: (TimeText) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentSave: () -> Unit,
    onRetryLoad: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    when (this) {
        SegmentViewState.Idle -> Unit
        is SegmentViewState.Data -> SegmentFormScene(
            state = this,
            onSegmentNameChange = onSegmentNameChange,
            onSegmentTimeChange = onSegmentTimeChange,
            onSegmentTimeTypeChange = onSegmentTimeTypeChange,
            onSegmentConfirmed = onSegmentSave,
            onSnackbarEvent = onSnackbarEvent
        )
        is SegmentViewState.Error -> SegmentErrorScene(
            throwable = throwable,
            onRetryLoadClick = onRetryLoad
        )
    }
}
