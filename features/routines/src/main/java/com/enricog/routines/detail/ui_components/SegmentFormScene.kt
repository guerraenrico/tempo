package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.EditingSegment
import com.enricog.routines.detail.models.Field
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.resources.dimensions

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
internal fun SegmentFormScene(
    data: EditingSegment.Data,
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
            .padding(MaterialTheme.dimensions.spaceM)
            .background(MaterialTheme.colors.background)
    ) {
        SegmentNameTextField(
            value = data.segment.name,
            onTextChange = onSegmentNameChange,
            errorMessageResourceId = data.errors[Field.Segment.Name]
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        SegmentTimeField(
            value = data.segment.timeInSeconds,
            onValueChange = onSegmentTimeChange,
            errorMessageResourceId = data.errors[Field.Segment.TimeInSeconds]
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        SelectableTimeType(
            timeTypes = data.timeTypes,
            selected = data.segment.type,
            onSelectChange = onSegmentTimeTypeChange
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        TempoButton(onClick = onSegmentBack, text = "BACK")
    }
}

@Composable
private fun SegmentNameTextField(
    value: String,
    onTextChange: (String) -> Unit,
    errorMessageResourceId: Int?
) {
    TempoTextField(
        value = value,
        onValueChange = onTextChange,
        isErrorValue = errorMessageResourceId != null,
        errorMessage = if (errorMessageResourceId != null) stringResource(errorMessageResourceId) else null
    )
}

@Composable
private fun SegmentTimeField(
    value: Long,
    onValueChange: (Long) -> Unit,
    errorMessageResourceId: Int?
) {
    TempoNumberField(
        value = value,
        onValueChange = onValueChange,
        isErrorValue = errorMessageResourceId != null,
        errorMessage = if (errorMessageResourceId != null) stringResource(errorMessageResourceId) else null
    )
}

@Composable
private fun SelectableTimeType(
    timeTypes: List<TimeType>,
    selected: TimeType,
    onSelectChange: (TimeType) -> Unit
) {
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
