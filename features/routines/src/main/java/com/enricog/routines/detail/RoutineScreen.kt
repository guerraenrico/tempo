package com.enricog.routines.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.routines.detail.models.RoutineViewState
import com.enricog.routines.detail.ui_components.RoutineDetail
import com.enricog.ui_components.ambients.navViewModel

@Composable
internal fun RoutineScreen(routineId: Long, viewModel: RoutineViewModel = navViewModel()) {
    onActive {
        viewModel.load(routineId)
    }
    val viewState by viewModel.viewState.collectAsState(RoutineViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        with(viewState) {
            when (this) {
                RoutineViewState.Idle -> {
                }
                is RoutineViewState.Data -> {
                    RoutineDetail(
                        state = this,
                        onRoutineNameChange = viewModel::onRoutineNameTextChange,
                        onStartTimeOffsetChange = viewModel::onRoutineStartTimeOffsetChange,
                        onAddSegmentClick = viewModel::onAddSegmentClick,
                        onSegmentClick = viewModel::onSegmentClick,
                        onSegmentDelete = viewModel::onSegmentDelete,
                        onSegmentNameChange = viewModel::onSegmentNameTextChange,
                        onSegmentTimeChange = viewModel::onSegmentTimeChange,
                        onSegmentTimeTypeChange = viewModel::onSegmentTypeChange,
                        onStartRoutine = viewModel::onStartRoutine,
                        onSegmentConfirmed = viewModel::onSegmentConfirmed,
                        onSegmentBack = viewModel::onSegmentBack,
                        onRoutineBack = viewModel::onRoutineBack
                    )
                }
            }.exhaustive
        }

    }
}