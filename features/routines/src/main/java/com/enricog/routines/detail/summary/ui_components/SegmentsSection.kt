package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.resources.dimensions

@Composable
internal fun SegmentsSection(
    segments: List<Segment>,
    onSegmentClick: (Segment) -> Unit,
    onSegmentDelete: (Segment) -> Unit,
    onAddSegmentClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimensions.spaceM),
    ) {
        val (label, buttonAdd) = createRefs()
        Text(
            modifier = Modifier.constrainAs(label) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(buttonAdd.bottom)
                end.linkTo(buttonAdd.start)
                width = Dimension.fillToConstraints
            },
            text = stringResource(R.string.field_label_routine_segments),
            style = MaterialTheme.typography.body2
        )
        TempoIconButton(
            modifier = Modifier.constrainAs(buttonAdd) {
                top.linkTo(parent.top)
                start.linkTo(label.end)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                baseline.linkTo(label.baseline)
            },
            onClick = onAddSegmentClick,
            size = TempoIconButtonSize.Small,
            icon = vectorResource(R.drawable.ic_segment_add),
            color = TempoButtonColor.Transparent,
            drawShadow = false
        )
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