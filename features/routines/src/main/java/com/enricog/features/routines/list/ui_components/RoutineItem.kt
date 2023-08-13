package com.enricog.features.routines.list.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.enricog.core.entities.ID
import com.enricog.features.routines.list.models.RoutinesItem.RoutineItem
import com.enricog.features.routines.ui_components.item.SwipeableListItem
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val RoutineItemTestTag = "RoutineItemTestTag"
internal const val RoutineItemCountTestTag = "RoutineItemCountTestTag"
internal const val RoutineItemEstimatedTotalTimeTestTag = "RoutineItemEstimatedTotalTimeTestTag"
internal const val RoutineItemSegmentTypeCountTestTag =
    "RoutineItemSegmentTypeCount_{{TYPE}}_TestTag"

@Composable
internal fun RoutineItem(
    routineItem: RoutineItem,
    enableClick: Boolean,
    onClick: (ID) -> Unit,
    onDelete: (ID) -> Unit,
    onDuplicate: (ID) -> Unit,
    modifier: Modifier = Modifier
) {
    SwipeableListItem(
        modifier = modifier
            .testTag(RoutineItemTestTag),
        onDelete = { onDelete(routineItem.id) },
        onDuplicate = { onDuplicate(routineItem.id) }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .clickable(enabled = enableClick) { onClick(routineItem.id) }
                .padding(TempoTheme.dimensions.spaceM)
        ) {
            val (routineName, goalText, count) = createRefs()
            TempoText(
                modifier = Modifier
                    .constrainAs(routineName) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        if (routineItem.segmentsSummary != null) {
                            end.linkTo(count.start)
                        } else {
                            end.linkTo(parent.end)
                        }
                        width = Dimension.fillToConstraints
                    },
                text = routineItem.name,
                style = TempoTheme.typography.h2,
            )
            if (routineItem.goalText != null) {
                TempoText(
                    modifier = Modifier
                        .constrainAs(goalText) {
                            top.linkTo(routineName.bottom)
                            start.linkTo(parent.start)
                        },
                    text = stringResource(
                        id = routineItem.goalText.stringResId,
                        formatArgs = routineItem.goalText.formatArgs.toTypedArray()
                    ),
                    style = TempoTheme.typography.body2,
                )
            }
            if (routineItem.segmentsSummary != null) {
                Counts(
                    modifier = Modifier
                        .padding(start = TempoTheme.dimensions.spaceL)
                        .constrainAs(count) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(routineName.end)
                            end.linkTo(parent.end)
                        },
                    segmentsSummary = routineItem.segmentsSummary
                )
            }
        }
    }
}

@Composable
private fun Counts(
    segmentsSummary: RoutineItem.SegmentsSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(RoutineItemCountTestTag),
        horizontalAlignment = Alignment.End
    ) {
        if (segmentsSummary.estimatedTotalTime != null) {
            TempoText(
                modifier = Modifier
                    .testTag(RoutineItemEstimatedTotalTimeTestTag)
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
                            RoutineItemSegmentTypeCountTestTag.replace(
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
