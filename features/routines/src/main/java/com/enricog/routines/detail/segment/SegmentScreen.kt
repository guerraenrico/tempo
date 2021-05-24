package com.enricog.routines.detail.segment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.segment.models.SegmentViewState
import com.enricog.routines.detail.segment.ui_components.SegmentFormScene
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun SegmentScreen(viewModel: SegmentViewModel) {
    val viewState by viewModel.viewState.collectAsState(SegmentViewState.Idle)
    Column(modifier = Modifier.fillMaxSize()) {
        TempoToolbar(onBack = viewModel::onSegmentBack)

        viewState.Compose(
            onSegmentNameChange = viewModel::onSegmentNameTextChange,
            onSegmentTimeChange = viewModel::onSegmentTimeChange,
            onSegmentTimeTypeChange = viewModel::onSegmentTypeChange,
            onSegmentConfirmed = viewModel::onSegmentConfirmed,
        )
    }
}

@Composable
internal fun SegmentViewState.Compose(
    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Long) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit
) {
    when (this) {
        SegmentViewState.Idle -> {
        }
        is SegmentViewState.Data -> {
            SegmentFormScene(
                segment = segment,
                errors = errors,
                timeTypes = timeTypes,
                onSegmentNameChange = onSegmentNameChange,
                onSegmentTimeChange = onSegmentTimeChange,
                onSegmentTimeTypeChange = onSegmentTimeTypeChange,
                onSegmentConfirmed = onSegmentConfirmed,
            )
        }
    }.exhaustive
}
