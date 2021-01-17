package com.enricog.routines.detail.segment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.routines.detail.segment.models.SegmentViewState
import com.enricog.routines.detail.segment.ui_components.SegmentFormScene
import com.enricog.ui_components.ambients.navViewModel
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun SegmentScreen(
    routineId: Long,
    segmentId: Long,
    viewModel: SegmentViewModel = navViewModel()
) {
    onActive { viewModel.load(routineId = routineId, segmentId = segmentId) }
    val viewState by viewModel.viewState.collectAsState(SegmentViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        TempoToolbar(onBack = viewModel::onSegmentBack)

        with (viewState) {
            when (this) {
                SegmentViewState.Idle -> {}
                is SegmentViewState.Data -> {
                    SegmentFormScene(
                        segment = segment,
                        errors = errors,
                        timeTypes = timeTypes,
                        onSegmentNameChange = viewModel::onSegmentNameTextChange,
                        onSegmentTimeChange = viewModel::onSegmentTimeChange,
                        onSegmentTimeTypeChange = viewModel::onSegmentTypeChange,
                        onSegmentConfirmed = viewModel::onSegmentConfirmed,
                    )
                }
            }.exhaustive
        }
    }
}