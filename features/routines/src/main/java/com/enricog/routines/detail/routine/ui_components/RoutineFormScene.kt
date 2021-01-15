package com.enricog.routines.detail.routine.ui_components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.routines.detail.routine.models.RoutineField
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.common.toolbar.TempoToolbar
import com.enricog.ui_components.resources.dimensions

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

@Composable
internal fun RoutineFormScene(
    routine: Routine,
    errors: Map<RoutineField, Int>,
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Long) -> Unit,
    onRoutineSave: () -> Unit,
    onRoutineBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineFormSceneTestTag)
    ) {
        TempoToolbar(onBack = onRoutineBack)

        ScrollableColumn(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)

        ) {
            RoutineNameTextField(
                value = routine.name,
                onTextChange = onRoutineNameChange,
                errorMessageResourceId = errors[RoutineField.Name]
            )

            RoutineStartTimeOffsetField(
                value = routine.startTimeOffsetInSeconds,
                onValueChange = onStartTimeOffsetChange,
                errorMessageResourceId = errors[RoutineField.StartTimeOffsetInSeconds]
            )
        }

        TempoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.spaceM),
            onClick = onRoutineSave,
            color = TempoButtonColor.Confirm,
            text = stringResource(R.string.button_save)
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
