package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.R
import com.enricog.routines.detail.models.Field
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.common.toolbar.TempoToolbar
import com.enricog.ui_components.modifiers.verticalListItemSpacing
import com.enricog.ui_components.resources.dimensions

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

@Composable
internal fun RoutineFormScene(
    routine: Routine,
    errors: Map<Field.Routine, Int>,
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Long) -> Unit,
    onAddSegmentClick: () -> Unit,
    onSegmentClick: (Segment) -> Unit,
    onStartRoutine: () -> Unit,
    onRoutineBack: () -> Unit
) {
    val startRoutineButtonSize = TempoIconButtonSize.Large
    val startRoutinePadding = MaterialTheme.dimensions.spaceL
    val segmentListBottomSpace = (startRoutinePadding * 2) + startRoutineButtonSize.box

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineFormSceneTestTag)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TempoToolbar(onBack = onRoutineBack)

            ScrollableColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                RoutineNameTextField(
                    value = routine.name,
                    onTextChange = onRoutineNameChange,
                    errorMessageResourceId = errors[Field.Routine.Name]
                )

                RoutineStartTimeOffsetField(
                    value = routine.startTimeOffsetInSeconds,
                    onValueChange = onStartTimeOffsetChange,
                    errorMessageResourceId = errors[Field.Routine.StartTimeOffsetInSeconds]
                )

                SegmentsSection(
                    segments = routine.segments,
                    onSegmentClick = onSegmentClick,
                    onAddSegmentClick = onAddSegmentClick
                )
                Spacer(Modifier.preferredHeight(segmentListBottomSpace))

                // TODO: handle no segment error
            }
        }
        TempoIconButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(startRoutinePadding),
            onClick = onStartRoutine,
            icon = vectorResource(R.drawable.ic_routine_play),
            color = TempoButtonColor.Accent,
            size = startRoutineButtonSize
        )
    }
}

@Composable
private fun RoutineNameTextField(
    value: String,
    onTextChange: (String) -> Unit,
    errorMessageResourceId: Int?
) {
    val errorMessage: String? = if (errorMessageResourceId != null) {
        stringResource(errorMessageResourceId)
    } else {
        null
    }
    TempoTextField(
        value = value,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.spaceM),
        label = stringResource(R.string.field_label_routine_name),
        errorMessage = errorMessage,
        singleLine = true
    )
}

@Composable
private fun RoutineStartTimeOffsetField(
    value: Long,
    onValueChange: (Long) -> Unit,
    errorMessageResourceId: Int?
) {
    val errorMessage: String? = if (errorMessageResourceId != null) {
        stringResource(errorMessageResourceId)
    } else {
        null
    }
    TempoNumberField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.spaceM),
        label = stringResource(R.string.field_label_routine_start_time_offset),
        errorMessage = errorMessage,
        singleLine = true
    )
}

@Composable
private fun SegmentsSection(
    segments: List<Segment>,
    onSegmentClick: (Segment) -> Unit,
    onAddSegmentClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimensions.spaceM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.field_label_routine_segments),
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.weight(1f))
        TempoIconButton(
            onClick = onAddSegmentClick,
            size = TempoIconButtonSize.Small,
            icon = vectorResource(R.drawable.ic_segment_add)
        )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        segments.mapIndexed { index, segment ->
            SegmentItem(
                segment = segment,
                onClick = onSegmentClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalListItemSpacing(
                        itemPosition = index,
                        spacing = MaterialTheme.dimensions.spaceM
                    )
            )
        }
    }
}