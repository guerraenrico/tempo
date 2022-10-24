package com.enricog.features.routines.detail.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.data.routines.api.entities.Segment
import com.enricog.features.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.features.routines.detail.summary.ui_components.RoutineSummaryErrorScene
import com.enricog.features.routines.detail.summary.ui_components.RoutineSummaryScene
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.toolbar.TempoToolbar

@Composable
internal fun RoutineSummaryScreen(viewModel: RoutineSummaryViewModel) {
    val viewState by viewModel.viewState.collectAsState(RoutineSummaryViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        TempoToolbar(onBack = viewModel::onBack)
        viewState.Compose(
            onSegmentAdd = viewModel::onSegmentAdd,
            onSegmentSelected = viewModel::onSegmentSelected,
            onSegmentDelete = viewModel::onSegmentDelete,
            onSegmentMoved = viewModel::onSegmentMoved,
            onRoutineStart = viewModel::onRoutineStart,
            onRoutineEdit = viewModel::onRoutineEdit,
            onRetryLoadClick = viewModel::onRetryLoadClick,
            onSnackbarEvent = viewModel::onSnackbarEvent
        )
    }
}

@Composable
internal fun RoutineSummaryViewState.Compose(
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onSegmentMoved: (Segment, Segment?) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit,
    onRetryLoadClick: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    when (this) {
        RoutineSummaryViewState.Idle -> Unit
        is RoutineSummaryViewState.Data -> {
            RoutineSummaryScene(
                summaryItems = items,
                message = message,
                onSegmentAdd = onSegmentAdd,
                onSegmentSelected = onSegmentSelected,
                onSegmentDelete = onSegmentDelete,
                onSegmentMoved = onSegmentMoved,
                onRoutineStart = onRoutineStart,
                onRoutineEdit = onRoutineEdit,
                onSnackbarEvent = onSnackbarEvent
            )
        }
        is RoutineSummaryViewState.Error -> RoutineSummaryErrorScene(
            throwable = throwable,
            onRetryLoadClick = onRetryLoadClick
        )
    }
}
