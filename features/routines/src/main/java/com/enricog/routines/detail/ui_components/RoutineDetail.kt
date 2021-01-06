package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.EditingSegmentView
import com.enricog.routines.detail.models.RoutineViewState

@Composable
internal fun RoutineDetail(
    state: RoutineViewState.Data,
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Long) -> Unit,
    onAddSegmentClick: () -> Unit,
    onSegmentClick: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onRoutineBack: () -> Unit,

    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Long) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit,
    onSegmentBack: () -> Unit,

    onStartRoutine: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RoutineFormScene(
            routine = state.routine,
            errors = state.errors,
            onRoutineNameChange = onRoutineNameChange,
            onStartTimeOffsetChange = onStartTimeOffsetChange,
            onAddSegmentClick = onAddSegmentClick,
            onSegmentClick = onSegmentClick,
            onSegmentDelete = onSegmentDelete,
            onStartRoutine = onStartRoutine,
            onRoutineBack = onRoutineBack
        )

        if (state.editingSegment is EditingSegmentView.Data) {
            SegmentFormScene(
                data = state.editingSegment,
                onSegmentNameChange = onSegmentNameChange,
                onSegmentTimeChange = onSegmentTimeChange,
                onSegmentTimeTypeChange = onSegmentTimeTypeChange,
                onSegmentConfirmed = onSegmentConfirmed,
                onSegmentBack = onSegmentBack
            )
        }
    }

}