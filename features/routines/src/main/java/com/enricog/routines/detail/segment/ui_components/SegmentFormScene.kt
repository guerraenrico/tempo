package com.enricog.routines.detail.segment.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.Seconds
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.R
import com.enricog.routines.detail.segment.models.SegmentField
import com.enricog.routines.detail.ui_components.TimeTypeChip
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.common.textField.TempoTimeField
import com.enricog.ui_components.modifiers.horizontalListItemSpacing
import com.enricog.ui_components.resources.TempoTheme

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
internal fun SegmentFormScene(
    segment: Segment,
    errors: Map<SegmentField, Int>,
    timeTypes: List<TimeType>,
    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Seconds) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(SegmentFormSceneTestTag)
            .fillMaxSize()
            .background(TempoTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState(0))
        ) {
            SegmentNameTextField(
                value = segment.name,
                onTextChange = onSegmentNameChange,
                errorMessageResourceId = errors[SegmentField.Name]
            )
            // TODO hide/disable time field if type selected is stopwatch
            SegmentTimeField(
                seconds = segment.time,
                onValueChange = onSegmentTimeChange,
                errorMessageResourceId = errors[SegmentField.TimeInSeconds],
            )
            SelectableTimeType(
                timeTypes = timeTypes,
                selected = segment.type,
                onSelectChange = onSegmentTimeTypeChange
            )
        }

        TempoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TempoTheme.dimensions.spaceM),
            onClick = onSegmentConfirmed,
            color = TempoButtonColor.Confirm,
            text = stringResource(R.string.button_save),
            contentDescription = stringResource(R.string.content_description_button_save_segment)
        )
    }
}

@Composable
private fun SegmentNameTextField(
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
            .padding(TempoTheme.dimensions.spaceM),
        label = stringResource(R.string.field_label_segment_name),
        errorMessage = errorMessage,
        singleLine = true
    )
}

@Composable
private fun SegmentTimeField(
    seconds: Seconds,
    onValueChange: (Seconds) -> Unit,
    errorMessageResourceId: Int?
) {
    val errorMessage: String? = if (errorMessageResourceId != null) {
        stringResource(errorMessageResourceId)
    } else {
        null
    }
    TempoTimeField(
        seconds = seconds,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(TempoTheme.dimensions.spaceM),
        label = stringResource(R.string.field_label_segment_time),
        errorMessage = errorMessage
    )
}

@Composable
private fun SelectableTimeType(
    timeTypes: List<TimeType>,
    selected: TimeType,
    onSelectChange: (TimeType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TempoTheme.dimensions.spaceM)
    ) {
        Text(
            text = stringResource(R.string.field_label_segment_type),
            style = TempoTheme.typography.body2
        )
        Spacer(Modifier.height(TempoTheme.dimensions.spaceM))
        Row {
            timeTypes.mapIndexed { index, timeType ->
                TimeTypeChip(
                    value = timeType,
                    isSelected = timeType == selected,
                    onSelect = onSelectChange,
                    modifier = Modifier.horizontalListItemSpacing(
                        itemPosition = index,
                        spacing = TempoTheme.dimensions.spaceS,
                        includeEdge = false
                    )
                )
            }
        }
    }
}
