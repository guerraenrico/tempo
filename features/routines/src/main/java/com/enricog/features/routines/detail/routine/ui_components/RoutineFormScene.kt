package com.enricog.features.routines.detail.routine.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineFields
import com.enricog.features.routines.detail.routine.models.RoutineViewState.Data.Message
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.icon.TempoIcon
import com.enricog.ui.components.icon.TempoIconSize
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.components.textField.TempoTextField
import com.enricog.ui.components.textField.TempoTimeField
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.theme.TempoTheme

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

@Composable
internal fun RoutineFormScene(
    routine: RoutineFields,
    errors: ImmutableMap<RoutineField, RoutineFieldError>,
    message: Message?,
    onRoutineNameChange: (TextFieldValue) -> Unit,
    onStartTimeOffsetChange: (TimeText) -> Unit,
    onStartTimeInfo: () -> Unit,
    onRoutineSave: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (routineNameRef, routineStartTimeRef) = remember { FocusRequester.createRefs() }
    val scrollState = rememberScrollState(initial = 0)
    val snackbarHostState = rememberSnackbarHostState()

    if (message != null) {
        val messageText = stringResource(id = message.textResId)
        val actionText = stringResource(id = message.actionTextResId)
        LaunchedEffect(snackbarHostState) {
            val event = snackbarHostState.show(message = messageText, actionText = actionText)
            onSnackbarEvent(event)
        }
    }

    TempoSnackbarHost(
        state = snackbarHostState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(RoutineFormSceneTestTag)
                    .padding(all = TempoTheme.dimensions.spaceM)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = scrollState)
                ) {
                    TempoTextField(
                        value = routine.name,
                        onValueChange = onRoutineNameChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(routineNameRef),
                        labelText = stringResource(R.string.field_label_routine_name),
                        errorText = stringResourceOrNull(id = errors[RoutineField.Name]?.stringResId),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { routineStartTimeRef.requestFocus() }
                        )
                    )

                    TempoTimeField(
                        value = routine.startTimeOffset,
                        onValueChange = onStartTimeOffsetChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(routineStartTimeRef),
                        labelText = stringResource(R.string.field_label_routine_start_time_offset),
                        supportingText = stringResource(R.string.field_support_text_routine_start_time_offset),
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        trailingIcon = {
                            StartTimeInfoIcon(onStartTimeInfo)
                        }
                    )
                }
            }
        },
        anchor = {
            TempoButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = TempoTheme.dimensions.spaceM),
                onClick = onRoutineSave,
                color = TempoButtonColor.Accent,
                text = stringResource(R.string.button_save),
                contentDescription = stringResource(R.string.content_description_button_save_routine)
            )
        }
    )
}

@Composable
private fun StartTimeInfoIcon(onClick: () -> Unit) {
    TempoIcon(
        iconResId = R.drawable.ic_routine_help,
        contentDescription = stringResource(R.string.content_description_button_help_routine_start_time),
        size = TempoIconSize.Original,
        color = TempoTheme.colors.onSurfaceSecondary,
        modifier = Modifier.clickable(
            role = Role.Button,
            enabled = true,
            onClick = onClick
        )
    )
} 
