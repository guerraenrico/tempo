package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize

internal const val SegmentSectionTitleTestTag = "SegmentSectionTitleTestTag"

@Composable
internal fun SegmentSectionTitle(
    item: RoutineSummaryItem.SegmentSectionTitle,
    onAddSegmentClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .testTag(SegmentSectionTitleTestTag)
            .fillMaxWidth()
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
                    if (item.error == null) {
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
            icon = vectorResource(R.drawable.ic_add),
            color = TempoButtonColor.TransparentPrimary,
            drawShadow = false,
            contentDescription = stringResource(R.string.content_description_button_add_segment)
        )

        if (item.error != null) {
            Text(
                modifier = Modifier.constrainAs(errorMessage) {
                    top.linkTo(buttonAdd.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                text = stringResource(item.error.second),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error
            )
        }
    }
}