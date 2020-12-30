package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.models.Field
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.resources.dimensions

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
private fun SegmentFormScene(
    segment: Segment,
    errors: Map<Field, Int>,
    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Long) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(SegmentFormSceneTestTag)
            .padding(MaterialTheme.dimensions.spaceM)
    ) {
        SegmentNameTextField(
            value = segment.name,
            onTextChange = onSegmentNameChange,
            errorMessageResourceId = errors[Field.Segment.Name]
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        SegmentTimeField(
            value = segment.timeInSeconds,
            onValueChange = onSegmentTimeChange,
            errorMessageResourceId = errors[Field.Segment.TimeInSeconds]
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
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
    selected: TimeType,
    onSelectChange: (TimeType) -> Unit
) {
    Row {
        Text()
        Text()
        Text()
    }
}