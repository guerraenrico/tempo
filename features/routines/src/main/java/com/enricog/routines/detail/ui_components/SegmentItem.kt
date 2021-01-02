package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Segment
import com.enricog.ui_components.common.surface.ListItemSurface
import com.enricog.ui_components.resources.dimensions

internal const val SegmentItemTestTag = "SegmentItemTestTag"

@Composable
internal fun SegmentItem(
    segment: Segment,
    onClick: (Segment) -> Unit,
    modifier: Modifier,
) {
    ListItemSurface(
        modifier = modifier
            .testTag(SegmentItemTestTag)
            .clickable { onClick(segment) }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.dimensions.spaceM)
        ) {
            val (name, type, time) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(name) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(time.start)
                        bottom.linkTo(type.top)
                        width = Dimension.fillToConstraints
                    },
                text = segment.name,
                style = MaterialTheme.typography.body1
            )

            TimeTypeChip(
                modifier = Modifier
                    .constrainAs(type) {
                        top.linkTo(name.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                value = segment.type,
                isSelected = false
            )

            Text(
                modifier = Modifier
                    .constrainAs(time) {
                        top.linkTo(parent.top)
                        start.linkTo(name.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                text = "${segment.timeInSeconds}s",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

