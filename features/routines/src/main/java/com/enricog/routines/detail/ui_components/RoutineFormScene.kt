package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.models.Field
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.resources.dimensions

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

// TODO: Improve UI

@Composable
internal fun RoutineFormScene(
    routine: Routine,
    errors: Map<Field.Routine, Int>,
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Long) -> Unit,
    onAddSegmentClick: () -> Unit,
    onSegmentClick: (Segment) -> Unit,
    onStartRoutine: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .testTag(RoutineFormSceneTestTag)
            .padding(MaterialTheme.dimensions.spaceM)
    ) {

        RoutineNameTextField(
            value = routine.name,
            onTextChange = onRoutineNameChange,
            errorMessageResourceId = errors[Field.Routine.Name]
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        RoutineStartTimeOffsetField(
            value = routine.startTimeOffsetInSeconds,
            onValueChange = onStartTimeOffsetChange,
            errorMessageResourceId = errors[Field.Routine.StartTimeOffsetInSeconds]
        )
        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))

        routine.segments.map { SegmentItem(it, onSegmentClick) }

        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        TempoButton(onClick = onAddSegmentClick, text = "Add Segment")

        Spacer(Modifier.height(MaterialTheme.dimensions.spaceM))
        TempoButton(onClick = onStartRoutine, text = "START")
    }
}

@Composable
private fun RoutineNameTextField(
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
private fun RoutineStartTimeOffsetField(
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
