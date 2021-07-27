package com.enricog.routines.detail.routine.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.entities.Seconds
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.routines.detail.routine.models.RoutineField
import com.enricog.ui_components.common.button.TempoButton
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.common.textField.TempoTimeField
import com.enricog.ui_components.resources.TempoTheme

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

@Composable
internal fun RoutineFormScene(
    routine: Routine,
    errors: Map<RoutineField, Int>,
    onRoutineNameChange: (String) -> Unit,
    onStartTimeOffsetChange: (Seconds) -> Unit,
    onRoutineSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(RoutineFormSceneTestTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState(initial = 0))
        ) {
            RoutineNameTextField(
                value = routine.name,
                onTextChange = onRoutineNameChange,
                errorMessageResourceId = errors[RoutineField.Name]
            )

            RoutineStartTimeOffsetField(
                seconds = routine.startTimeOffset,
                onValueChange = onStartTimeOffsetChange,
                errorMessageResourceId = errors[RoutineField.StartTimeOffsetInSeconds]
            )
        }

        TempoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TempoTheme.dimensions.spaceM),
            onClick = onRoutineSave,
            color = TempoButtonColor.Confirm,
            text = stringResource(R.string.button_save),
            contentDescription = stringResource(R.string.content_description_button_save_routine)
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
            .padding(TempoTheme.dimensions.spaceM),
        label = stringResource(R.string.field_label_routine_name),
        errorMessage = errorMessage,
        singleLine = true
    )
}

@Composable
private fun RoutineStartTimeOffsetField(
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
        label = stringResource(R.string.field_label_routine_start_time_offset),
        errorMessage = errorMessage,
    )
}
