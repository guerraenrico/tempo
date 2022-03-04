package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui.components.common.button.TempoButtonColor
import com.enricog.ui.components.common.button.TempoIconButton
import com.enricog.ui.components.common.button.TempoIconButtonSize
import com.enricog.ui.components.resources.TempoTheme

internal const val SegmentSectionTitleTestTag = "SegmentSectionTitleTestTag"

@Composable
internal fun SegmentSectionTitle(
    modifier: Modifier = Modifier,
    item: RoutineSummaryItem.SegmentSectionTitle,
    onAddSegmentClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
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
            style = TempoTheme.typography.h5,
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
            icon = painterResource(R.drawable.ic_add),
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
                style = TempoTheme.typography.caption,
                color = TempoTheme.colors.error
            )
        }
    }
}
