package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.core.entities.ID
import com.enricog.core.entities.asID
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.features.routines.detail.ui.time_type.TimeTypeChip
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import com.enricog.features.routines.ui_components.SwipeableListItem
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme

internal const val SegmentItemTestTag = "SegmentItemTestTag"

@Composable
internal fun SegmentItem(
    modifier: Modifier,
    segment: SegmentItem,
    enableClick: Boolean,
    onClick: (ID) -> Unit,
    onDelete: (ID) -> Unit,
    onDuplicate: (ID) -> Unit
) {
    SwipeableListItem(
        modifier = modifier
            .testTag(SegmentItemTestTag),
        onDelete = { onDelete(segment.id) },
        onDuplicate = { onDuplicate(segment.id) }
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
                style = segment.type
            )

            TempoText(
                modifier = Modifier
                    .constrainAs(time) {
                        top.linkTo(parent.top)
                        start.linkTo(name.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                text = segment.time.toStringFormatted(),
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
            time = "100".timeText,
            type = TimeTypeStyle(
                id = "TIMER",
                backgroundColor = Color.Blue,
                onBackgroundColor = Color.White,
                nameStringResId = R.string.chip_time_type_timer_name
            ),
            rank = ""
        ),
        onClick = {},
        onDelete = {},
        onDuplicate = {},
        enableClick = true
    )
}
