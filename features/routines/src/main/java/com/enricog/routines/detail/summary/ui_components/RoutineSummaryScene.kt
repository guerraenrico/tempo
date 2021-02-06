package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.enricog.core.extensions.exhaustive
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.resources.dimensions

internal const val RoutineSummarySceneTestTag = "RoutineSummaryScene"

@Composable
internal fun RoutineSummaryScene(
    summaryItems: List<RoutineSummaryItem>,
    onSegmentAdd: () -> Unit,
    onSegmentSelected: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onRoutineStart: () -> Unit,
    onRoutineEdit: () -> Unit
) {
    val startRoutineButtonSize = TempoIconButtonSize.Large
    val startRoutinePadding = MaterialTheme.dimensions.spaceM
    val segmentListBottomSpace = startRoutinePadding + startRoutineButtonSize.box

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineSummarySceneTestTag)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spaceM),
            contentPadding = PaddingValues(MaterialTheme.dimensions.spaceM)
        ) {
            itemsIndexed(summaryItems) { index, item ->
                when (item) {
                    is RoutineSummaryItem.RoutineInfo -> {
                        RoutineSection(
                            routineName = item.routineName,
                            onEditRoutine = onRoutineEdit
                        )
                    }
                    is RoutineSummaryItem.SegmentSectionTitle -> {
                        SegmentSectionTitle(item, onSegmentAdd)
                    }
                    is RoutineSummaryItem.SegmentItem -> {
                        SegmentItem(
                            segment = item.segment,
                            onClick = onSegmentSelected,
                            onDelete = onSegmentDelete,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }.exhaustive
                if (index == summaryItems.size - 1) {
                    Spacer(Modifier.preferredHeight(segmentListBottomSpace))
                }
            }
        }

        TempoIconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(startRoutinePadding),
            onClick = onRoutineStart,
            icon = vectorResource(R.drawable.ic_routine_play),
            color = TempoButtonColor.Accent,
            size = startRoutineButtonSize,
            contentDescription = stringResource(R.string.content_description_button_start_routine)
        )
    }
}
