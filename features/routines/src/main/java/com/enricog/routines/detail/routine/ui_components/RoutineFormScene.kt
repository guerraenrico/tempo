package com.enricog.routines.detail.routine.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.routines.detail.routine.models.RoutineField
import com.enricog.ui_components.common.textField.TempoNumberField
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.common.toolbar.TempoToolbar
import com.enricog.ui_components.resources.dimensions

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

@Composable
internal fun RoutineFormScene(
    routine: Routine,
    errors: Map<RoutineField.Routine, Int>,
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Long) -> Unit,
    onRoutineBack: () -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineFormSceneTestTag)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TempoToolbar(onBack = onRoutineBack)

            RoutineNameTextField(
                value = routine.name,
                onTextChange = onRoutineNameChange,
                errorMessageResourceId = errors[RoutineField.Routine.Name]
            )

            RoutineStartTimeOffsetField(
                value = routine.startTimeOffsetInSeconds,
                onValueChange = onStartTimeOffsetChange,
                errorMessageResourceId = errors[RoutineField.Routine.StartTimeOffsetInSeconds]
            )
        }
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
