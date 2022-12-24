package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.features.routines.detail.ui.time_type.TimeTypeChip
import com.enricog.features.routines.ui_components.DeletableListItem
import com.enricog.ui.components.extensions.format
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

internal const val SegmentItemTestTag = "SegmentItemTestTag"

@Composable
internal fun SegmentItem(
    modifier: Modifier,
    segment: SegmentItem,
    enableClick: Boolean,
    onClick: (ID) -> Unit,
    onDelete: (ID) -> Unit
) {
    DeletableListItem(
        modifier = modifier
            .testTag(SegmentItemTestTag),
        onDelete = { onDelete(segment.id) }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enableClick) { onClick(segment.id) }
                .padding(TempoTheme.dimensions.spaceM)
        ) {
            val (name, type, time) = createRefs()

            TempoText(
                modifier = Modifier
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(time.start)
                        bottom.linkTo(type.top)
                        width = Dimension.fillToConstraints
                    },
                text = segment.name,
                style = TempoTheme.typography.h2
            )

            TimeTypeChip(
                modifier = Modifier
                    .constrainAs(type) {
                        top.linkTo(name.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = TempoTheme.dimensions.spaceM),
                value = segment.type
            )

            TempoText(
                modifier = Modifier
                    .constrainAs(time) {
                        top.linkTo(parent.top)
                        start.linkTo(name.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                text = segment.time.format(),
                style = TempoTheme.typography.h3
            )
        }
    }
}

@Preview
@Composable
private fun SegmentItemPreview() {
    SegmentItem(
        modifier = Modifier,
        segment = SegmentItem(
            id = 0.asID,
            name = "Segment name",
            time = 100.seconds,
            type = TimeType.from(TimeTypeEntity.TIMER),
            rank = ""
        ),
        onClick = {},
        onDelete = {},
        enableClick = true
    )
}
