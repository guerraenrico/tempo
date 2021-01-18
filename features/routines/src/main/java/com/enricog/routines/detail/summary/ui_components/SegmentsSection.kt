package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryField
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.resources.dimensions

internal const val SegmentsSectionTestTag = "SegmentsSectionTestTag"

@Composable
internal fun SegmentsSection(
    segments: List<Segment>,
    error: Pair<RoutineSummaryField.Segments, Int>?,
    onSegmentClick: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onAddSegmentClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .testTag(SegmentsSectionTestTag)
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimensions.spaceM),
    ) {
        val (label, buttonAdd, errorMessage) = createRefs()
        Text(
            modifier = Modifier.constrainAs(label) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(buttonAdd.bottom)
                end.linkTo(buttonAdd.start)
                width = Dimension.fillToConstraints
            },
            text = stringResource(R.string.field_label_routine_segments),
            style = MaterialTheme.typography.h5,
            letterSpacing = 1.sp
        )
        TempoIconButton(
            modifier = Modifier.constrainAs(buttonAdd) {
                top.linkTo(parent.top)
                start.linkTo(label.end)
                bottom.linkTo(
                    if (error == null) {
                        parent.bottom
                    } else {
                        errorMessage.top
                    }
                )
                end.linkTo(parent.end)
                baseline.linkTo(label.baseline)
            },
            onClick = onAddSegmentClick,
            size = TempoIconButtonSize.Small,
            icon = vectorResource(R.drawable.ic_segment_add),
            color = TempoButtonColor.TransparentPrimary,
            drawShadow = false
        )

        if (error != null) {
            Text(
                modifier = Modifier.constrainAs(errorMessage) {
                    top.linkTo(buttonAdd.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                text = stringResource(error.second),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.spaceM),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spaceM),
    ) {
        segments.map { segment ->
            SegmentItem(
                segment = segment,
                onClick = onSegmentClick,
                onDelete = onSegmentDelete,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}