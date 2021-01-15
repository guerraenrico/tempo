package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.common.toolbar.TempoToolbar
import com.enricog.ui_components.resources.dimensions

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"

@Composable
internal fun RoutineSummaryScene(
    segments: List<Segment>,
    onAddSegmentClick: () -> Unit,
    onSegmentClick: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onStartRoutine: () -> Unit,
    onBack: () -> Unit
) {
    val startRoutineButtonSize = TempoIconButtonSize.Large
    val startRoutinePadding = MaterialTheme.dimensions.spaceL
    val segmentListBottomSpace = (startRoutinePadding * 2) + startRoutineButtonSize.box

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSummarySceneTestTag)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TempoToolbar(onBack = onBack)

            ScrollableColumn(
                modifier = Modifier.fillMaxSize()
            ) {


                SegmentsSection(
                    segments = segments,
                    onSegmentClick = onSegmentClick,
                    onSegmentDelete = onSegmentDelete,
                    onAddSegmentClick = onAddSegmentClick
                )

                Spacer(Modifier.preferredHeight(segmentListBottomSpace))
            }
        }

        TempoIconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(startRoutinePadding),
            onClick = onStartRoutine,
            icon = vectorResource(R.drawable.ic_routine_play),
            color = TempoButtonColor.Accent,
            size = startRoutineButtonSize
        )
    }
}