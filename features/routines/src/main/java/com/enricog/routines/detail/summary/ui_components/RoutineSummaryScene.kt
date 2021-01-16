package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.resources.dimensions

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"

@Composable
internal fun RoutineSummaryScene(
    routine: Routine,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit
) {
    val startRoutineButtonSize = TempoIconButtonSize.Large
    val startRoutinePadding = MaterialTheme.dimensions.spaceL
    val segmentListBottomSpace = (startRoutinePadding * 2) + startRoutineButtonSize.box

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSummarySceneTestTag)
    ) {

        ScrollableColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            RoutineSection(
                routine = routine,
                onEditRoutine = onRoutineEdit
            )
            Spacer(Modifier.preferredHeight(MaterialTheme.dimensions.spaceL))

            SegmentsSection(
                segments = routine.segments,
                onSegmentClick = onSegmentSelected,
                onSegmentDelete = onSegmentDelete,
                onAddSegmentClick = onSegmentAdd
            )
            Spacer(Modifier.preferredHeight(segmentListBottomSpace))
        }

        TempoIconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(startRoutinePadding),
            onClick = onRoutineStart,
            icon = vectorResource(R.drawable.ic_routine_play),
            color = TempoButtonColor.Accent,
            size = startRoutineButtonSize
        )
    }
}