package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.core.compose.api.modifiers.spacing.horizontalListItemSpacing
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo.SegmentsSummary
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.extensions.format
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.contentColorFor

internal const val RoutineSectionTestTag = "RoutineSectionTestTag"
internal const val RoutineSectionCountTestTag = "RoutineSectionCountTestTag"
internal const val RoutineSectionTotalTimeTestTag = "RoutineSectionTotalTimeTestTag"
internal const val RoutineSectionSegmentTypeCountTestTag =
    "RoutineSectionSegmentTypeCount_{{TYPE}}_TestTag"

@Composable
internal fun RoutineSection(
    routineInfo: RoutineInfo,
    onEditRoutine: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .testTag(RoutineSectionTestTag)
            .fillMaxWidth()
    ) {
        val (name, buttonEdit, count) = createRefs()
        TempoText(
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(buttonEdit.top)
                end.linkTo(count.start)
                width = Dimension.fillToConstraints
            },
            text = routineInfo.routineName,
            style = TempoTheme.typography.h1
        )

        TempoIconButton(
            modifier = Modifier.constrainAs(buttonEdit) {
                top.linkTo(name.bottom)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            },
            onClick = onEditRoutine,
            size = TempoIconButtonSize.Small,
            iconResId = R.drawable.ic_routine_edit,
            color = TempoButtonColor.TransparentSecondary,
            drawShadow = false,
            contentDescription = stringResource(R.string.content_description_button_edit_routine)
        )
        if (routineInfo.segmentsSummary != null) {
            Counts(
                modifier = Modifier.constrainAs(count) {
                    top.linkTo(parent.top)
                    start.linkTo(name.end)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    baseline.linkTo(name.baseline)
                },
                segmentsSummary = routineInfo.segmentsSummary
            )
        }
    }
}

@Composable
private fun Counts(
    segmentsSummary: SegmentsSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(RoutineSectionCountTestTag),
        horizontalAlignment = Alignment.End
    ) {
        if (segmentsSummary.totalTime != null) {
            TempoText(
                modifier = Modifier
                    .testTag(RoutineSectionTotalTimeTestTag)
                    .padding(bottom = TempoTheme.dimensions.spaceXS),
                text = segmentsSummary.totalTime.format(),
                style = TempoTheme.typography.h2,
                textAlign = TextAlign.End
            )
        }
        Row {
            segmentsSummary.segmentTypesCount.onEachIndexed { index, (timeType, count) ->
                Box(
                    modifier = Modifier
                        .horizontalListItemSpacing(
                            itemPosition = index,
                            spacingHorizontal = 5.dp,
                            spacingVertical = 0.dp,
                            includeEdge = false
                        )
                        .size(21.dp)
                        .clip(CircleShape)
                        .background(color = timeType.color, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    TempoText(
                        modifier = Modifier.testTag(
                            RoutineSectionSegmentTypeCountTestTag.replace(
                                oldValue = "{{TYPE}}",
                                newValue = timeType.id
                            )
                        ),
                        text = count.toString(),
                        style = TempoTheme.typography.h6.copy(
                            color = TempoTheme.colors.contentColorFor(
                                backgroundColor = timeType.color
                            )
                        )
                    )
                }
            }
        }
    }
}
