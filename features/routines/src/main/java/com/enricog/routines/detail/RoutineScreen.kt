package com.enricog.routines.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.RoutineViewState
import com.enricog.routines.detail.ui_components.RoutineDetail
import com.enricog.ui_components.ambients.navViewModel

@Composable
internal fun RoutineScreen(routineId: Long, viewModel: RoutineViewModel = navViewModel()) {
    onActive {
        viewModel.load(routineId)
    }
    val viewState = viewModel.viewState.collectAsState(RoutineViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        viewState.value.Compose(
            onRoutineNameChange = viewModel::onRoutineNameTextChange,
            onStartTimeOffsetChange = viewModel::onRoutineStartTimeOffsetChange,
            onAddSegmentClick = viewModel::onAddSegmentClick,
            onSegmentClick = viewModel::onSegmentClick,
            onSegmentNameChange = viewModel::onSegmentNameTextChange,
            onSegmentTimeChange = viewModel::onSegmentTimeChange,
            onSegmentTimeTypeChange = viewModel::onSegmentTypeChange,
            onStartRoutine = viewModel::onStartRoutine,
            onSegmentConfirmed = viewModel::onSegmentConfirmed,
            onSegmentBack = viewModel::onSegmentBack,
            onRoutineBack = viewModel::onRoutineBack
        )
    }
}

@Composable
private fun RoutineViewState.Compose(
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Long) -> Unit,
    onAddSegmentClick: () -> Unit,
    onSegmentClick: (Segment) -> Unit,
    onRoutineBack: () -> Unit,

    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Long) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit,
    onSegmentBack: () -> Unit,

    onStartRoutine: () -> Unit
) {
    when (this) {
        RoutineViewState.Idle -> {
        }
        is RoutineViewState.Data -> {
            RoutineDetail(
                state = this,
                onRoutineNameChange = onRoutineNameChange,
                onStartTimeOffsetChange = onStartTimeOffsetChange,
                onAddSegmentClick = onAddSegmentClick,
                onSegmentClick = onSegmentClick,
                onSegmentNameChange = onSegmentNameChange,
                onSegmentTimeChange = onSegmentTimeChange,
                onSegmentTimeTypeChange = onSegmentTimeTypeChange,
                onStartRoutine = onStartRoutine,
                onSegmentConfirmed = onSegmentConfirmed,
                onSegmentBack = onSegmentBack,
                onRoutineBack = onRoutineBack
            )
        }
    }.exhaustive
}