package com.enricog.routines.detail.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import com.enricog.routines.detail.summary.ui_components.RoutineSummaryScene
import com.enricog.ui_components.ambients.navViewModel
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun RoutineSummaryScreen(
    routineId: Long,
    viewModel: RoutineSummaryViewModel = navViewModel()
) {
    onActive { viewModel.load(routineId) }
    val viewState by viewModel.viewState.collectAsState(RoutineSummaryViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        TempoToolbar(onBack = viewModel::onBack)

        with(viewState) {
            when (this) {
                RoutineSummaryViewState.Idle -> {
                }
                is RoutineSummaryViewState.Data -> {
                    RoutineSummaryScene(
                        segments = routine.segments,
                        onAddSegmentClick = viewModel::onAddSegmentClick,
                        onSegmentClick = viewModel::onSegmentClick,
                        onSegmentDelete = viewModel::onSegmentDelete,
                        onStartRoutine = viewModel::onStartRoutine
                    )
                }
            }
        }
    }
}