package com.enricog.routines.detail.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.ui_components.RoutineSummaryScene
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun RoutineSummaryScreen(
    routineId: Long,
    viewModel: RoutineSummaryViewModel
) {
    DisposableEffect(routineId) {
        viewModel.load(routineId)
        onDispose {}
    }
    val viewState by viewModel.viewState.collectAsState(RoutineSummaryViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        TempoToolbar(onBack = viewModel::onBack)
        viewState.Compose(
            onSegmentAdd = viewModel::onSegmentAdd,
            onSegmentSelected = viewModel::onSegmentSelected,
            onSegmentDelete = viewModel::onSegmentDelete,
            onRoutineStart = viewModel::onRoutineStart,
            onRoutineEdit = viewModel::onRoutineEdit
        )
    }
}

@Composable
internal fun RoutineSummaryViewState.Compose(
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit
) {
    when (this) {
        RoutineSummaryViewState.Idle -> {
        }
        is RoutineSummaryViewState.Data -> {
            RoutineSummaryScene(
                summaryItems = items,
                onSegmentAdd = onSegmentAdd,
                onSegmentSelected = onSegmentSelected,
                onSegmentDelete = onSegmentDelete,
                onRoutineStart = onRoutineStart,
                onRoutineEdit = onRoutineEdit
            )
        }
    }
}