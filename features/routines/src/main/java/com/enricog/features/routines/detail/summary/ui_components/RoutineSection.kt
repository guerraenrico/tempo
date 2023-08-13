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
import com.enricog.features.routines.ui_components.goal_label.GoalText
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val RoutineSectionTestTag = "RoutineSectionTestTag"
internal const val RoutineSectionSummaryInfoTestTag = "RoutineSectionSummaryInfoTestTag"
internal const val RoutineSectionEstimatedTotalTimeTestTag = "RoutineSectionEstimatedTotalTimeTestTag"
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
        val (routineName, goalText, buttonEdit, segmentCount) = createRefs()
        TempoText(
            modifier = Modifier.constrainAs(routineName) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                if (routineInfo.goalLabel != null) {
                    bottom.linkTo(goalText.top)
                } else {
                    bottom.linkTo(buttonEdit.top)
                }
                end.linkTo(segmentCount.start)
                width = Dimension.fillToConstraints
            },
            text = routineInfo.routineName,
            style = TempoTheme.typography.h1
        )

        if (routineInfo.goalLabel != null) {
            GoalText(
                modifier = Modifier.constrainAs(goalText) {
                    top.linkTo(routineName.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(buttonEdit.top)
                },
                label = routineInfo.goalLabel,
            )
        }

        TempoButton(
            modifier = Modifier.constrainAs(buttonEdit) {
                if (routineInfo.goalLabel != null) {
                    top.linkTo(goalText.bottom)
                } else {
                    top.linkTo(routineName.bottom)
                }
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            },
            onClick = onEditRoutine,
            iconResId = R.drawable.ic_routine_edit,
            text = stringResource(R.string.button_routine_summary_edit_routine),
            color = TempoButtonColor.TransparentSecondary,
            iconContentDescription = stringResource(R.string.content_description_button_edit_routine)
        )

        if (routineInfo.segmentsSummary != null) {
            SegmentSummary(
                modifier = Modifier
                    .padding(start = TempoTheme.dimensions.spaceM)
                    .constrainAs(segmentCount) {
                        top.linkTo(parent.top)
                        start.linkTo(routineName.end)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        baseline.linkTo(routineName.baseline)
                    },
                segmentsSummary = routineInfo.segmentsSummary
            )
        }
    }
}

@Composable
private fun SegmentSummary(
    segmentsSummary: SegmentsSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(RoutineSectionSummaryInfoTestTag),
        horizontalAlignment = Alignment.End
    ) {
        if (segmentsSummary.estimatedTotalTime != null) {
            TempoText(
                modifier = Modifier
                    .testTag(RoutineSectionEstimatedTotalTimeTestTag)
                    .padding(bottom = TempoTheme.dimensions.spaceXS),
                text = segmentsSummary.estimatedTotalTime.toStringFormatted(),
                style = TempoTheme.typography.h3,
                textAlign = TextAlign.End
            )
        }
        Row {
            segmentsSummary.segmentTypesCount.onEachIndexed { index, (timeTypeStyle, count) ->
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
                        .background(color = timeTypeStyle.backgroundColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    TempoText(
                        modifier = Modifier.testTag(
                            RoutineSectionSegmentTypeCountTestTag.replace(
                                oldValue = "{{TYPE}}",
                                newValue = timeTypeStyle.id
                            )
                        ),
                        text = count.toString(),
                        style = TempoTheme.typography.h6.copy(
                            color = timeTypeStyle.onBackgroundColor
                        )
                    )
                }
            }
        }
    }
}
