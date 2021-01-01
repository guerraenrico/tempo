package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.routines.TimeType
import com.enricog.routines.R
import com.enricog.routines.detail.models.EditingSegmentView
import com.enricog.routines.detail.models.Field
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.common.toolbar.TempoToolbar
import com.enricog.ui_components.resources.dimensions

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
internal fun SegmentFormScene(
    data: EditingSegmentView.Data,
    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Long) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit,
    onSegmentBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .testTag(SegmentFormSceneTestTag)
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        TempoToolbar(onBack = onSegmentBack)

        ScrollableColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SegmentNameTextField(
                value = data.segment.name,
                onTextChange = onSegmentNameChange,
                errorMessageResourceId = data.errors[Field.Segment.Name]
            )
            // TODO hide/disable time field if type selected is stopwatch
            SegmentTimeField(
                value = data.segment.timeInSeconds,
                onValueChange = onSegmentTimeChange,
                errorMessageResourceId = data.errors[Field.Segment.TimeInSeconds],
            )
            SelectableTimeType(
                timeTypes = data.timeTypes,
                selected = data.segment.type,
                onSelectChange = onSegmentTimeTypeChange
            )
        }

        TempoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.spaceM),
            onClick = onSegmentConfirmed,
            color = TempoButtonColor.Confirm,
            text = stringResource(R.string.button_save)
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
            .padding(MaterialTheme.dimensions.spaceM),
        label = stringResource(R.string.field_label_segment_name),
        isErrorValue = errorMessage != null,
        errorMessage = errorMessage
    )
}

@Composable
private fun SegmentTimeField(
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
        label = stringResource(R.string.field_label_segment_time),
        isErrorValue = errorMessage != null,
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
            .padding(MaterialTheme.dimensions.spaceM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.field_label_segment_type),
            style = MaterialTheme.typography.caption
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        Row {
            timeTypes.map { timeType ->
                TimeTypeChip(
                    value = timeType,
                    isSelected = timeType == selected,
                    onSelect = onSelectChange
                )
            }
        }
    }
}
