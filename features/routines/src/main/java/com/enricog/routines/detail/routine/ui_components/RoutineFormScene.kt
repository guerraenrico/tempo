package com.enricog.routines.detail.routine.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.enricog.core.compose.api.extensions.stringResourceOrNull
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
    val keyboardController = LocalSoftwareKeyboardController.current
    val (routineNameRef, routineStartTimeRef) = remember { FocusRequester.createRefs() }

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
            TempoTextField(
                value = routine.name,
                onValueChange = onRoutineNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TempoTheme.dimensions.spaceM)
                    .focusRequester(routineNameRef),
                label = stringResource(R.string.field_label_routine_name),
                errorMessage = stringResourceOrNull(id = errors[RoutineField.Name]),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { routineStartTimeRef.requestFocus() }
                )
            )

            TempoTimeField(
                seconds = routine.startTimeOffset,
                onValueChange = onStartTimeOffsetChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TempoTheme.dimensions.spaceM)
                    .focusRequester(routineStartTimeRef),
                label = stringResource(R.string.field_label_routine_start_time_offset),
                errorMessage = stringResourceOrNull(id = errors[RoutineField.StartTimeOffsetInSeconds]),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
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
