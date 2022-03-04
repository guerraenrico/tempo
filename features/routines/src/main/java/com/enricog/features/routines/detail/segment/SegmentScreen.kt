package com.enricog.features.routines.detail.segment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.enricog.base.extensions.exhaustive
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.Seconds
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.ui_components.SegmentFormScene
import com.enricog.ui.components.common.toolbar.TempoToolbar

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
    onSegmentTimeChange: (Seconds) -> Unit,
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
